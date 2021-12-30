package com.fantechs.provider.guest.callagv.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.agv.dto.PositionCodePath;
import com.fantechs.common.base.agv.dto.RcsResponseDTO;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarehouseAreaDto;
import com.fantechs.common.base.general.dto.callagv.*;
import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouseArea;
import com.fantechs.common.base.general.entity.callagv.*;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvAgvTask;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvStorageMaterial;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchHrUserInfo;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.general.entity.tem.search.SearchTemVehicle;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.agv.AgvFeignApi;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.guest.callagv.mapper.*;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleReBarcodeService;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CallAgvVehicleReBarcodeServiceImpl extends BaseService<CallAgvVehicleReBarcode> implements CallAgvVehicleReBarcodeService {

    @Resource
    private CallAgvVehicleReBarcodeMapper callAgvVehicleReBarcodeMapper;

    @Resource
    private CallAgvVehicleLogMapper callAgvVehicleLogMapper;

    @Resource
    private CallAgvProductionInLogMapper callAgvProductionInLogMapper;

    @Resource
    private CallAgvAgvTaskMapper callAgvAgvTaskMapper;

    @Resource
    private CallAgvAgvTaskBarcodeMapper callAgvAgvTaskBarcodeMapper;

    @Resource
    private CallAgvBarcodeMapper callAgvBarcodeMapper;

    @Resource
    private CallAgvWarehouseAreaReBarcodeMapper callAgvWarehouseAreaReBarcodeMapper;

    @Resource
    private CallAgvWarehouseAreaBarcodeLogMapper callAgvWarehouseAreaBarcodeLogMapper;

    @Resource
    private TemVehicleFeignApi temVehicleFeignApi;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    private AgvFeignApi agvFeignApi;

//    @Resource
//    private MulinsenFeignApi mulinsenFeignApi;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SCMSign scmSign;

    @Value("${SCM.url}")
    private String url;

    @Value("${SCM.pkGroup}")
    private String pkGroup;

    @Value("${SCM.pkOrg}")
    private String pkOrg;

    @Value("${SCM.factoryCode}")
    private String factoryCode;

    @Value("${SCM.appKey}")
    private String appKey;

    @Value("${SCM.secret}")
    private String secret;

    @Override
    public List<CallAgvVehicleReBarcodeDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return callAgvVehicleReBarcodeMapper.findList(map);
    }

    @Override
    @Transactional
    @LcnTransaction
    public List<CallAgvVehicleReBarcode> callAgvStock(RequestCallAgvStockDTO requestCallAgvStockDTO) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        TemVehicle temVehicle = temVehicleFeignApi.detail(requestCallAgvStockDTO.getVehicleId()).getData();
        if (temVehicle.getStatus() != 1) {
            throw new BizErrorException("货架不是空闲状态，不能进行备料！");
        }
        SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
        searchCallAgvAgvTask.setVehicleId(requestCallAgvStockDTO.getVehicleId());
        searchCallAgvAgvTask.setTaskStatusList(Arrays.asList(1,2));
        List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
        if (!callAgvAgvTaskDtoList.isEmpty()) {
            throw new BizErrorException("当前货架存在未完成任务，请等待任务结束");
        }

        SearchBaseStorageTaskPoint searchBaseStorageTaskPoint = new SearchBaseStorageTaskPoint();
        searchBaseStorageTaskPoint.setStorageTaskPointId(requestCallAgvStockDTO.getStorageTaskPointId());
        List<BaseStorageTaskPoint> baseStorageTaskPointList = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData();
        BaseStorageTaskPoint baseStorageTaskPoint = baseStorageTaskPointList.get(0);
        if (baseStorageTaskPoint.getStorageTaskPointStatus() != 1 && StringUtils.isNotEmpty(baseStorageTaskPoint.getVehicleId())
                && !requestCallAgvStockDTO.getVehicleId().equals(baseStorageTaskPoint.getVehicleId())) {
            throw new BizErrorException("当前库位配送点已绑定相应的货架，请重新扫码备料！");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("barcodeIdList", requestCallAgvStockDTO.getBarcodeIdList());
        List<CallAgvVehicleReBarcodeDto> callAgvVehicleReBarcodeDtoList = findList(map);
        if (!callAgvVehicleReBarcodeDtoList.isEmpty()) {
            List<String> barCodeList = new LinkedList<>();
            for (CallAgvVehicleReBarcodeDto callAgvVehicleReBarcodeDto : callAgvVehicleReBarcodeDtoList) {
                barCodeList.add(callAgvVehicleReBarcodeDto.getBarcode());
            }
            throw new BizErrorException("条码：" + barCodeList.toString() + "已绑定其他货架，不能重复备料！");
        }
        List<CallAgvVehicleReBarcode> callAgvVehicleReBarcodeList = new LinkedList<>();
        List<GoodsDetail> goodsDetails = new LinkedList<>();
        for (Long id : requestCallAgvStockDTO.getBarcodeIdList()) {
            CallAgvVehicleReBarcode callAgvVehicleReBarcode = new CallAgvVehicleReBarcode();
            callAgvVehicleReBarcode.setBarcodeId(id);
            callAgvVehicleReBarcode.setVehicleId(requestCallAgvStockDTO.getVehicleId());
            callAgvVehicleReBarcode.setStatus((byte) 1);
            callAgvVehicleReBarcode.setOrgId(user.getOrganizationId());
            callAgvVehicleReBarcode.setCreateUserId(user.getUserId());
            callAgvVehicleReBarcode.setCreateTime(new Date());
            callAgvVehicleReBarcode.setIsDelete((byte) 1);
            callAgvVehicleReBarcodeMapper.insertUseGeneratedKeys(callAgvVehicleReBarcode);
            callAgvVehicleReBarcodeList.add(callAgvVehicleReBarcode);

            CallAgvVehicleLog callAgvVehicleLog = new CallAgvVehicleLog();
            callAgvVehicleLog.setBarcodeId(id);
            callAgvVehicleLog.setVehicleId(requestCallAgvStockDTO.getVehicleId());
            callAgvVehicleLog.setStartStorageTaskPointId(requestCallAgvStockDTO.getStorageTaskPointId());
            callAgvVehicleLog.setOperatorType((byte) 1);
            callAgvVehicleLog.setOrgId(user.getOrganizationId());
            callAgvVehicleLog.setCreateUserId(user.getUserId());
            callAgvVehicleLog.setCreateTime(new Date());
            callAgvVehicleLogMapper.insertSelective(callAgvVehicleLog);

            CallAgvBarcode callAgvBarcode = callAgvBarcodeMapper.selectByPrimaryKey(id);
            callAgvBarcode.setBarcodeStatus((byte) 2);
            callAgvBarcode.setModifiedUserId(user.getUserId());
            callAgvBarcode.setModifiedTime(new Date());
            callAgvBarcodeMapper.updateByPrimaryKeySelective(callAgvBarcode);

            GoodsDetail goodsDetail = new GoodsDetail();
            goodsDetail.setBatchNumber(callAgvBarcode.getBatch());
            goodsDetail.setBarCode(callAgvBarcode.getBarcode());
            goodsDetail.setActualQuantity(callAgvBarcode.getQty().doubleValue());
            goodsDetail.setMaterialCodes(callAgvBarcode.getMaterialCode());
            goodsDetails.add(goodsDetail);
        }
        temVehicle.setVehicleStatus((byte) 3);
        temVehicle.setStorageTaskPointId(requestCallAgvStockDTO.getStorageTaskPointId());
        temVehicle.setModifiedUserId(user.getUserId());
        temVehicle.setModifiedTime(new Date());
        temVehicleFeignApi.update(temVehicle);

        baseStorageTaskPoint.setStorageTaskPointStatus((byte) 2);
        baseStorageTaskPoint.setModifiedUserId(user.getUserId());
        baseStorageTaskPoint.setModifiedTime(new Date());
        baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPoint);

        // 备料入库，生成生产报告
        Map<String, Object> paramMap = productionReportParam(baseStorageTaskPoint, temVehicle, user, goodsDetails);
        log.info("货架 : " + temVehicle.getVehicleCode() + "备料入库，生成生产报告 : " + JsonUtils.objectToJson(paramMap));
        String result = RestTemplateUtil.postForString(url, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        log.info("货架 : " + temVehicle.getVehicleCode() + "备料入库，生成生产报告结果 : " + jsonObject);
        if (!"0".equals(jsonObject.get("code"))) {
            throw new BizErrorException("生成生产报告失败：" + jsonObject.get("msg"));
        }

        return callAgvVehicleReBarcodeList;
    }

    @Override
    @Transactional
    @LcnTransaction
    public String callAgvDistribution(Long vehicleId, Long warehouseAreaId, Long storageTaskPointId, Integer type, Boolean isSchedulingTask) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
        searchCallAgvAgvTask.setVehicleId(vehicleId);
        if (isSchedulingTask) {
            searchCallAgvAgvTask.setTaskStatusList(Arrays.asList(1, 2));
        } else {
            searchCallAgvAgvTask.setTaskStatusList(Arrays.asList(2));
        }
        List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
        if (!callAgvAgvTaskDtoList.isEmpty()) {
            throw new BizErrorException("当前货架存在未完成任务，请等待任务结束");
        }
        List<String> positionCodeList = new LinkedList<>();
        TemVehicle temVehicle = temVehicleFeignApi.detail(vehicleId).getData();
        if (StringUtils.isEmpty(temVehicle.getAgvTaskTemplate())) {
            throw new BizErrorException("请先维护该货架对应的AGV任务模板");
        }
        if (type == 2 && temVehicle.getVehicleStatus() == 1) {
            throw new BizErrorException("空货架不能进行叫料");
        }
        if (type == 3 && temVehicle.getVehicleStatus() != 1) {
            throw new BizErrorException("非空货架不能进行空货架返回操作");
        }
        if (StringUtils.isEmpty(temVehicle.getStorageTaskPointId()) || temVehicle.getStorageTaskPointId() == 0) {
            throw new BizErrorException("货架没有绑定当前的配送点，无法进行配送");
        }
        SearchBaseStorageTaskPoint searchBaseStorageTaskPoint1 = new SearchBaseStorageTaskPoint();
        searchBaseStorageTaskPoint1.setStorageTaskPointId(temVehicle.getStorageTaskPointId());
        List<BaseStorageTaskPoint> baseStorageTaskPoints = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint1).getData();
        BaseStorageTaskPoint baseStorageTaskPoint = baseStorageTaskPoints.get(0);
        if (type == 1 && baseStorageTaskPoint.getTaskPointType() == 2) {
            throw new BizErrorException("该货架已经位于存料区域，无需进行配送");
        }
        positionCodeList.add(baseStorageTaskPoint.getXyzCode());

        BaseStorageTaskPoint baseStorageTaskPointEnd = new BaseStorageTaskPoint();
        if (storageTaskPointId == 0) {
            SearchBaseStorageTaskPoint searchBaseStorageTaskPoint = new SearchBaseStorageTaskPoint();
            searchBaseStorageTaskPoint.setWarehouseAreaId(warehouseAreaId);
            if (type == 1) {
                searchBaseStorageTaskPoint.setTaskPointType((byte) 2);
            }
            searchBaseStorageTaskPoint.setStorageTaskPointStatus((byte) 1);
            searchBaseStorageTaskPoint.setIfOrderByUsePriority(1);
            List<BaseStorageTaskPoint> baseStorageTaskPointList = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData();
            if (StringUtils.isEmpty(baseStorageTaskPointList)) {
                throw new BizErrorException("目的区域没有空闲的库位，请稍后再试");
            }
            positionCodeList.add(baseStorageTaskPointList.get(0).getXyzCode());
            baseStorageTaskPointEnd = baseStorageTaskPointList.get(0);
        } else {
            SearchBaseStorageTaskPoint searchBaseStorageTaskPoint = new SearchBaseStorageTaskPoint();
            searchBaseStorageTaskPoint.setStorageTaskPointId(storageTaskPointId);
            baseStorageTaskPointEnd = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData().get(0);
            if (baseStorageTaskPointEnd.getStorageTaskPointStatus() != 1) {
                throw new BizErrorException("目的库位对应的配送点被占用，请重新选择");
            }
            positionCodeList.add(baseStorageTaskPointEnd.getXyzCode());
        }

        String message = "";
        if (type == 1) {
            message = "备料配送";
        } else if (type == 2) {
            message = "叫料配送";
        } else {
            message = "空货架返回";
        }

        // 判断目的库区是否已达到最大使用率
        BaseWarehouseArea baseWarehouseArea = baseFeignApi.warehouseAreaDetail(warehouseAreaId).getData();
        if (StringUtils.isNotEmpty(baseWarehouseArea.getMaxUsageRate())) {
            SearchBaseStorageTaskPoint searchBaseStorageTaskPoint = new SearchBaseStorageTaskPoint();
            searchBaseStorageTaskPoint.setStartPage(1);
            searchBaseStorageTaskPoint.setPageSize(9999);
            searchBaseStorageTaskPoint.setWarehouseAreaId(warehouseAreaId);
            searchBaseStorageTaskPoint.setTaskPointType((byte) 2);
            int total = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData().size();
            searchBaseStorageTaskPoint.setStorageTaskPointStatus((byte) 2);
            int count = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData().size() + 1;
            BigDecimal usageRate = BigDecimal.valueOf(count * 100).divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_DOWN);
            if (usageRate.compareTo(baseWarehouseArea.getMaxUsageRate()) == 1) {
                throw new BizErrorException("目的库区使用率已达到最大使用率 : " + usageRate + "%" + "，暂不能进行" + message + "操作，请稍后再试");
            }
        }

        Boolean taskBoolean = false;
        String taskTyp = temVehicle.getAgvTaskTemplate();
        if (StringUtils.isNotEmpty(baseStorageTaskPoint.getType(),
                baseStorageTaskPointEnd.getType())
                && !baseStorageTaskPoint.getType().equals(baseStorageTaskPointEnd.getType())) {
            taskTyp = temVehicle.getAgvTaskTemplateSecond();

            taskBoolean = true;
        }

        if (taskBoolean && StringUtils.isNotEmpty(redisUtil.get(baseStorageTaskPoint.getType())) && isSchedulingTask) {
            // 记录等待中的AGV任务
            CallAgvAgvTask callAgvAgvTask = new CallAgvAgvTask();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            callAgvAgvTask.setTaskCode(taskTyp + "-" + temVehicle.getVehicleCode() + "-" + simpleDateFormat.format(new Date()));
            callAgvAgvTask.setVehicleId(vehicleId);
            callAgvAgvTask.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
            callAgvAgvTask.setTaskStatus((byte) 1);
            callAgvAgvTask.setOperateType(type.byteValue());
            callAgvAgvTask.setStatus((byte) 1);
            callAgvAgvTask.setOrgId(user.getOrganizationId());
            callAgvAgvTask.setCreateUserId(user.getUserId());
            callAgvAgvTask.setCreateTime(new Date());
            callAgvAgvTask.setIsDelete((byte) 1);
            callAgvAgvTaskMapper.insertUseGeneratedKeys(callAgvAgvTask);

            log.info("记录货架 : " + temVehicle.getVehicleCode() + "对应的电梯等待任务 : " + callAgvAgvTask.getAgvTaskId());
            redisUtil.set(temVehicle.getVehicleCode(), callAgvAgvTask.getAgvTaskId());

            List<CallAgvDistributionDto> callAgvDistributionDtoList = BeanUtils.convertJson(redisUtil.get(baseStorageTaskPoint.getType()).toString(), new TypeToken<List<CallAgvDistributionDto>>() {
            }.getType());
            // 添加电梯等待队列任务
            CallAgvDistributionDto callAgvDistributionDto = new CallAgvDistributionDto();
            callAgvDistributionDto.setVehicleId(vehicleId);
            callAgvDistributionDto.setVehicleCode(temVehicle.getVehicleCode());
            callAgvDistributionDto.setWarehouseAreaId(warehouseAreaId);
            callAgvDistributionDto.setStorageTaskPointId(storageTaskPointId);
            callAgvDistributionDto.setType(type);
            callAgvDistributionDto.setAgvTaskId(callAgvAgvTask.getAgvTaskId());
            callAgvDistributionDtoList.add(callAgvDistributionDto);
            redisUtil.set(baseStorageTaskPoint.getType(), JSONObject.toJSONString(callAgvDistributionDtoList));
            log.info("==========记录AGV电梯任务队列 : key " + baseStorageTaskPoint.getType() + " , value : " + JSONObject.toJSONString(callAgvDistributionDtoList));

            return callAgvAgvTask.getTaskCode();
        }

        temVehicle.setStorageTaskPointId(0l);
        temVehicle.setModifiedUserId(user.getUserId());
        temVehicle.setModifiedTime(new Date());

        baseStorageTaskPointEnd.setStorageTaskPointStatus((byte) 2);
        baseStorageTaskPointEnd.setModifiedUserId(user.getUserId());
        baseStorageTaskPointEnd.setModifiedTime(new Date());
        baseStorageTaskPointEnd.setRemark("锁定配送目的点，等待货架：" + temVehicle.getVehicleCode() + "配送");
        baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPointEnd);

        if (type == 2 || type == 3) {
            CallAgvVehicleLog callAgvVehicleLog = new CallAgvVehicleLog();
            callAgvVehicleLog.setVehicleId(vehicleId);
            callAgvVehicleLog.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
            callAgvVehicleLog.setEndStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
            if (type == 2) {
                callAgvVehicleLog.setOperatorType((byte) 2);
            } else {
                callAgvVehicleLog.setOperatorType((byte) 4);
            }
            callAgvVehicleLog.setOrgId(user.getOrganizationId());
            callAgvVehicleLog.setCreateUserId(user.getUserId());
            callAgvVehicleLog.setCreateTime(new Date());
            callAgvVehicleLogMapper.insertSelective(callAgvVehicleLog);
        }

        Example example = new Example(CallAgvVehicleReBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vehicleId", vehicleId).andEqualTo("orgId", user.getOrganizationId());
        List<CallAgvVehicleReBarcode> callAgvVehicleReBarcodeList = callAgvVehicleReBarcodeMapper.selectByExample(example);

        List<GoodsDetail> goodsDetails = new LinkedList<>();
        List<MlsIsAvailableDetail> list = new LinkedList<>();
        if (type != 3) {
            // 插入条码出入库操作记录
            for (CallAgvVehicleReBarcode callAgvVehicleReBarcode : callAgvVehicleReBarcodeList) {
                CallAgvProductionInLog callAgvProductionInLog = new CallAgvProductionInLog();
                callAgvProductionInLog.setInWarehouseId(baseStorageTaskPoint.getWarehouseId());
                callAgvProductionInLog.setOutWarehouseId(baseStorageTaskPointEnd.getWarehouseId());
                callAgvProductionInLog.setInWarehouseAreaId(baseStorageTaskPoint.getWarehouseAreaId());
                callAgvProductionInLog.setOutWarehouseAreaId(baseStorageTaskPointEnd.getWarehouseAreaId());
                callAgvProductionInLog.setInStorageId(baseStorageTaskPoint.getStorageId());
                callAgvProductionInLog.setOutStorageId(baseStorageTaskPointEnd.getStorageId());
                callAgvProductionInLog.setBarcodeId(callAgvVehicleReBarcode.getBarcodeId());
                callAgvProductionInLog.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
                callAgvProductionInLog.setEndStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
                callAgvProductionInLog.setOperateTime(new Date());
                callAgvProductionInLog.setOrgId(user.getOrganizationId());
                callAgvProductionInLog.setCreateUserId(user.getUserId());
                callAgvProductionInLog.setCreateTime(new Date());
                callAgvProductionInLogMapper.insertSelective(callAgvProductionInLog);

                CallAgvBarcode callAgvBarcode = callAgvBarcodeMapper.selectByPrimaryKey(callAgvVehicleReBarcode.getBarcodeId());
                if (type == 1) {
                    callAgvBarcode.setBarcodeStatus((byte) 3);
                } else if (type == 2) {
                    callAgvBarcode.setBarcodeStatus((byte) 4);
                }
                callAgvBarcode.setModifiedUserId(user.getUserId());
                callAgvBarcode.setModifiedTime(new Date());
                callAgvBarcodeMapper.updateByPrimaryKeySelective(callAgvBarcode);

                GoodsDetail goodsDetail = new GoodsDetail();
                goodsDetail.setBatchNumber(callAgvBarcode.getBatch());
                goodsDetail.setBarCode(callAgvBarcode.getBarcode());
                goodsDetail.setActualQuantity(callAgvBarcode.getQty().doubleValue());
                goodsDetail.setMaterialCodes(callAgvBarcode.getMaterialCode());
                goodsDetails.add(goodsDetail);

                MlsIsAvailableDetail mlsIsAvailableDetail = new MlsIsAvailableDetail();
                mlsIsAvailableDetail.setMaterialCode(callAgvBarcode.getMaterialCode());
                mlsIsAvailableDetail.setNumber(callAgvBarcode.getQty().longValue());
                list.add(mlsIsAvailableDetail);
            }
        }

        // 叫料确认/配送入库（库位转移）
        Map<String, Object> map = new HashMap<>();
        if (type != 3) {
            if (baseStorageTaskPoint.getWarehouseId().equals(baseStorageTaskPointEnd.getWarehouseId())) {
                Map<String, String> jsonMap = new HashMap<>();
                jsonMap.put("inStrogeteCode", baseStorageTaskPointEnd.getStorageCode());
                jsonMap.put("containerCode", temVehicle.getVehicleCode());
                log.info("配送入库参数：" + JSONObject.toJSONString(jsonMap));
                map = scmParam(jsonMap, "mls.save.production.transfer");
            } else {
                // 判断是否有可用的备料计划
                if (baseStorageTaskPointEnd.getTaskPointType() == 2 && false) {
                    MlsIsAvailable mlsIsAvailable = new MlsIsAvailable();
                    mlsIsAvailable.setFactoryCode(factoryCode);

                    SearchHrUserInfo searchHrUserInfo = new SearchHrUserInfo();
//                    searchHrUserInfo.setJobNum(user.getUserCode());
//                    List<HrUserInfoDto> hrUserInfoDtoList = mulinsenFeignApi.findList(searchHrUserInfo).getData();
//                    if (hrUserInfoDtoList.isEmpty() || StringUtils.isEmpty(hrUserInfoDtoList.get(0).getErpDeptcode())) {
//                        throw new BizErrorException("没有找到当前用户的部门，请检查ERP用户和部门的关联数据");
//                    }
//                    mlsIsAvailable.setDeptCode(hrUserInfoDtoList.get(0).getErpDeptcode());
                    mlsIsAvailable.setList(list);
                    log.info("判断是否有可用的备料计划参数：" + JSONObject.toJSONString(mlsIsAvailable));
                    Map<String, Object> paramMap = scmParam(mlsIsAvailable, "mls.is.available.plan");
                    log.info("货架 : " + temVehicle.getVehicleCode() + "叫料配送出库，判断是否有可用的备料计划" + " : " + JsonUtils.objectToJson(paramMap));
                    String result = RestTemplateUtil.postForString(url, paramMap);
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    log.info("货架 : " + temVehicle.getVehicleCode() + "叫料配送出库，判断是否有可用的备料计划" + "结果 : " + jsonObject);
                    if (!"0".equals(jsonObject.get("code"))) {
                        throw new BizErrorException(message + "失败：" + jsonObject.get("msg"));
                    }
                }
                map = outWarehouse(baseStorageTaskPoint, baseStorageTaskPointEnd, user, goodsDetails);
            }
        }

        // 记录AGV任务
        CallAgvAgvTask callAgvAgvTask = new CallAgvAgvTask();
        callAgvAgvTask.setVehicleId(vehicleId);
        callAgvAgvTask.setTaskStatus((byte) 2);
        callAgvAgvTask.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
        callAgvAgvTask.setEndStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
        callAgvAgvTask.setOperateType(type.byteValue());
        callAgvAgvTask.setStatus((byte) 1);
        callAgvAgvTask.setOrgId(user.getOrganizationId());
        callAgvAgvTask.setCreateUserId(user.getUserId());
        callAgvAgvTask.setCreateTime(new Date());
        callAgvAgvTask.setIsDelete((byte) 1);

        String taskCode = "";
        try {
            log.info("==========启动agv执行" + message + "作业任务==============\r\n");
            taskCode = genAgvSchedulingTask(taskTyp, positionCodeList, temVehicle.getVehicleCode());

            if (StringUtils.isNotEmpty(map)) {
                redisUtil.set("scm-" + taskCode, JSONObject.toJSONString(map));
                log.info("=========记录当前" + message + "作业对应的出库单 : key : " + "scm-" + taskCode + " value : " + JSONObject.toJSONString(map) + "\r\n");
            }

            temVehicle.setRemark(taskCode);
            temVehicleFeignApi.update(temVehicle);

            baseStorageTaskPoint.setStorageTaskPointStatus((byte) 1);
            baseStorageTaskPoint.setModifiedUserId(user.getUserId());
            redisUtil.set("3-" + taskCode, JSONObject.toJSONString(baseStorageTaskPoint));
            log.info("=========记录当前" + message + "作业对应的起始配送点 : key : " + "3-" + taskCode + " value : " + JSONObject.toJSONString(baseStorageTaskPoint) + "\r\n");

            temVehicle.setStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
            redisUtil.set("2-" + taskCode, JSONObject.toJSONString(temVehicle));
            log.info("=========记录当前" + message + "作业载具对应的目的配送点 : key : " + "2-" + taskCode + " value : " + JSONObject.toJSONString(temVehicle) + "\r\n");

            if (taskBoolean) {
                redisUtil.set("4-" + taskCode, baseStorageTaskPoint.getType());
                log.info("=========记录AGV电梯任务作业对应的配送方式 : key : " + "4-" + taskCode + " value : " + baseStorageTaskPoint.getType() + "\r\n");

                if (isSchedulingTask) {
                    List<CallAgvDistributionDto> callAgvDistributionDtoList = new ArrayList<>();
                    // 添加电梯等待队列任务
                    CallAgvDistributionDto callAgvDistributionDto = new CallAgvDistributionDto();
                    callAgvDistributionDto.setVehicleId(vehicleId);
                    callAgvDistributionDto.setVehicleCode(temVehicle.getVehicleCode());
                    callAgvDistributionDto.setWarehouseAreaId(warehouseAreaId);
                    callAgvDistributionDto.setStorageTaskPointId(storageTaskPointId);
                    callAgvDistributionDto.setType(type);
                    callAgvDistributionDto.setAgvTaskId(callAgvAgvTask.getAgvTaskId());
                    callAgvDistributionDtoList.add(callAgvDistributionDto);
                    redisUtil.set(baseStorageTaskPoint.getType(), JSONObject.toJSONString(callAgvDistributionDtoList));
                    log.info("==========记录AGV电梯任务队列 : key " + baseStorageTaskPoint.getType() + " , value : " + JSONObject.toJSONString(callAgvDistributionDtoList));
                }
            }
        } catch (BizErrorException e) {
            throw new BizErrorException("启动agv执行" + message + "作业任务失败" + e.getMessage());
        }

        if (StringUtils.isEmpty(redisUtil.get(temVehicle.getVehicleCode()))) {
            callAgvAgvTask.setTaskCode(taskCode);
            callAgvAgvTaskMapper.insertUseGeneratedKeys(callAgvAgvTask);
        } else {
            CallAgvAgvTask callAgvAgvTask2 = callAgvAgvTaskMapper.selectByPrimaryKey(Long.parseLong(redisUtil.get(temVehicle.getVehicleCode()).toString()));
            callAgvAgvTask.setAgvTaskId(Long.parseLong(redisUtil.get(temVehicle.getVehicleCode()).toString()));
            callAgvAgvTask.setRemark(callAgvAgvTask2.getTaskCode());
            callAgvAgvTask.setTaskCode(taskCode);
            callAgvAgvTask.setModifiedTime(new Date());
            callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
        }
        if(type != 3) {
            List<CallAgvAgvTaskBarcode> callAgvAgvTaskBarcodeList = new LinkedList<>();
            for (CallAgvVehicleReBarcode callAgvVehicleReBarcode : callAgvVehicleReBarcodeList) {
                CallAgvAgvTaskBarcode callAgvAgvTaskBarcode = new CallAgvAgvTaskBarcode();
                callAgvAgvTaskBarcode.setAgvTaskId(callAgvAgvTask.getAgvTaskId());
                callAgvAgvTaskBarcode.setBarcodeId(callAgvVehicleReBarcode.getBarcodeId());
                callAgvAgvTaskBarcode.setStatus((byte) 1);
                callAgvAgvTaskBarcode.setOrgId(user.getOrganizationId());
                callAgvAgvTaskBarcode.setCreateTime(new Date());
                callAgvAgvTaskBarcode.setCreateUserId(user.getUserId());
                callAgvAgvTaskBarcodeList.add(callAgvAgvTaskBarcode);
            }
            callAgvAgvTaskBarcodeMapper.insertList(callAgvAgvTaskBarcodeList);
        }

        return taskCode;
    }

    @Override
    @Transactional
    @LcnTransaction
    public String callAgvDistributionRest(CallAgvDistributionRestDto callAgvDistributionRestDto) throws Exception {
        if (StringUtils.isEmpty(callAgvDistributionRestDto.getVehicleCode(),
                callAgvDistributionRestDto.getWarehouseAreaCode(),
                callAgvDistributionRestDto.getType())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        SearchTemVehicle searchTemVehicle = new SearchTemVehicle();
        searchTemVehicle.setVehicleCode(callAgvDistributionRestDto.getVehicleCode());
        List<TemVehicleDto> temVehicleDtoList = temVehicleFeignApi.findList(searchTemVehicle).getData();
        if (temVehicleDtoList.isEmpty() || temVehicleDtoList.size() > 1) {
            throw new BizErrorException("没有找到对应的货架，请输入正确完整的货架编码");
        }
        Long vehicleId = temVehicleDtoList.get(0).getVehicleId();

        SearchBaseWarehouseArea searchBaseWarehouseArea = new SearchBaseWarehouseArea();
        searchBaseWarehouseArea.setWarehouseAreaCode(callAgvDistributionRestDto.getWarehouseAreaCode());
        List<BaseWarehouseAreaDto> baseWarehouseAreaDtoList = baseFeignApi.findWarehouseAreaList(searchBaseWarehouseArea).getData();
        if (baseWarehouseAreaDtoList.isEmpty() || baseWarehouseAreaDtoList.size() > 1) {
            throw new BizErrorException("没有找到对应的目的库区，请输入正确完整的库区编码");
        }
        Long warehouseAreaId = baseWarehouseAreaDtoList.get(0).getWarehouseAreaId();
        Long storageTaskPointId = 0l;

        if (StringUtils.isNotEmpty(callAgvDistributionRestDto.getStorageCode())) {
            SearchBaseStorageTaskPoint searchBaseStorageTaskPoint = new SearchBaseStorageTaskPoint();
            searchBaseStorageTaskPoint.setStorageCode(callAgvDistributionRestDto.getStorageCode());
            List<BaseStorageTaskPoint> baseStorageTaskPointList = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData();
            if (baseWarehouseAreaDtoList.isEmpty() || baseWarehouseAreaDtoList.size() > 1) {
                throw new BizErrorException("没有找到对应的目的库位对应的配送点，请输入正确完整的库位编码或检查该库位是否维护相应的配送点");
            }
            warehouseAreaId = baseStorageTaskPointList.get(0).getWarehouseAreaId();
            storageTaskPointId = baseStorageTaskPointList.get(0).getStorageTaskPointId();
        }


        String taskCode = callAgvDistribution(vehicleId, warehouseAreaId, storageTaskPointId, callAgvDistributionRestDto.getType(), true);

        return taskCode;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int vehicleBarcodeUnbound(RequestBarcodeUnboundDTO requestBarcodeUnboundDTO) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
        searchCallAgvAgvTask.setVehicleId(requestBarcodeUnboundDTO.getVehicleId());
        searchCallAgvAgvTask.setTaskStatusList(Arrays.asList(1,2));
        List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
        if (!callAgvAgvTaskDtoList.isEmpty()) {
            throw new BizErrorException("当前货架存在未完成任务，请等待任务结束");
        }
        TemVehicle temVehicle = temVehicleFeignApi.detail(requestBarcodeUnboundDTO.getVehicleId()).getData();
        SearchBaseStorageTaskPoint searchBaseStorageTaskPoint = new SearchBaseStorageTaskPoint();
        searchBaseStorageTaskPoint.setStorageTaskPointId(temVehicle.getStorageTaskPointId());
        List<BaseStorageTaskPoint> baseStorageTaskPointList = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData();
        BaseStorageTaskPoint baseStorageTaskPoint = baseStorageTaskPointList.get(0);
        if (requestBarcodeUnboundDTO.getType() == 1) {
            temVehicle.setVehicleStatus((byte) 1);
            temVehicle.setVehicleId(requestBarcodeUnboundDTO.getVehicleId());
            temVehicle.setModifiedUserId(user.getUserId());
            temVehicle.setModifiedTime(new Date());
            temVehicleFeignApi.update(temVehicle);
        }

        int barcodeStatus = 0;
        for (Long id : requestBarcodeUnboundDTO.getVehicleReBarcodeIdList()) {
            CallAgvVehicleReBarcode callAgvVehicleReBarcode = callAgvVehicleReBarcodeMapper.selectByPrimaryKey(id);
            CallAgvVehicleLog callAgvVehicleLog = new CallAgvVehicleLog();
            callAgvVehicleLog.setBarcodeId(callAgvVehicleReBarcode.getBarcodeId());
            callAgvVehicleLog.setVehicleId(callAgvVehicleReBarcode.getVehicleId());
            callAgvVehicleLog.setEndStorageTaskPointId(temVehicle.getStorageTaskPointId());
            callAgvVehicleLog.setOperatorType((byte) 3);
            callAgvVehicleLog.setOrgId(user.getOrganizationId());
            callAgvVehicleLog.setCreateUserId(user.getUserId());
            callAgvVehicleLog.setCreateTime(new Date());
            callAgvVehicleLogMapper.insertSelective(callAgvVehicleLog);

            CallAgvBarcode callAgvBarcode = callAgvBarcodeMapper.selectByPrimaryKey(callAgvVehicleReBarcode.getBarcodeId());
            barcodeStatus = callAgvBarcode.getBarcodeStatus().intValue();
            callAgvBarcode.setBarcodeStatus((byte) 5);
            callAgvBarcode.setModifiedUserId(user.getUserId());
            callAgvBarcode.setModifiedTime(new Date());
            callAgvBarcodeMapper.updateByPrimaryKeySelective(callAgvBarcode);
        }

        List<GoodsDetail> materialDetails = new LinkedList<>();
        if (requestBarcodeUnboundDTO.getType() == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("vehicleId", requestBarcodeUnboundDTO.getVehicleId());
            map.put("orgId", user.getOrganizationId());
            List<CallAgvVehicleReBarcodeDto> callAgvVehicleReBarcodeDtoList = callAgvVehicleReBarcodeMapper.findList(map);
            for (CallAgvVehicleReBarcodeDto callAgvVehicleReBarcodeDto : callAgvVehicleReBarcodeDtoList) {
                if (!requestBarcodeUnboundDTO.getVehicleReBarcodeIdList().contains(callAgvVehicleReBarcodeDto.getVehicleReBarcodeId())) {
                    GoodsDetail materialDetail = new GoodsDetail();
                    materialDetail.setBatchNumber(callAgvVehicleReBarcodeDto.getBatch());
                    materialDetail.setBarCode(callAgvVehicleReBarcodeDto.getBarcode());
                    materialDetail.setActualQuantity(callAgvVehicleReBarcodeDto.getQty().doubleValue());
                    materialDetail.setMaterialCodes(callAgvVehicleReBarcodeDto.getMaterialCode());
                    materialDetails.add(materialDetail);
                }
            }
        }

        Example example = new Example(CallAgvVehicleReBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("vehicleReBarcodeId", requestBarcodeUnboundDTO.getVehicleReBarcodeIdList());
        callAgvVehicleReBarcodeMapper.deleteByExample(example);

        // 取消绑定货架，删除生产报告/叫料解绑
        MlsDeleteProductionReport mlsDeleteProductionReport = new MlsDeleteProductionReport();
        mlsDeleteProductionReport.setIsUnbound(requestBarcodeUnboundDTO.getType());
        mlsDeleteProductionReport.setWarehouseCode(baseStorageTaskPoint.getWarehouseCode());
        mlsDeleteProductionReport.setContainerCode(temVehicle.getVehicleCode());
        mlsDeleteProductionReport.setUserName("0504291");
        if (requestBarcodeUnboundDTO.getType() == 0) {
            mlsDeleteProductionReport.setMaterialDetails(materialDetails);
        }
        log.info("取消绑定货架，删除生产报告/叫料解绑参数：" + JSONObject.toJSONString(mlsDeleteProductionReport));
        Map<String, Object> paramMap = new HashMap<>();
        String message = "";
        if (barcodeStatus == 4) {
            message = "叫料解绑";
            paramMap = scmParam(mlsDeleteProductionReport, "mls.unbound.goods");
        } else {
            message = "取消绑定货架，删除生产报告";
            paramMap = scmParam(mlsDeleteProductionReport, "mls.delete.production.report");
        }
        log.info(message + " : " + JsonUtils.objectToJson(paramMap));
        String result = RestTemplateUtil.postForString(url, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        log.info(message + "结果 : " + jsonObject);
        if (!"0".equals(jsonObject.get("code"))) {
            throw new BizErrorException("物料解绑失败：" + jsonObject.get("msg"));
        }

        return requestBarcodeUnboundDTO.getVehicleReBarcodeIdList().size();
    }

    @Override
    public List<CallAgvVehicleBarcodeDTO> findCallAgvVehicleList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("taskPointType", 2);
        map.put("orgId", user.getOrganizationId());
        map.put("groupByVehicle", 1);
        List<CallAgvVehicleReBarcodeDto> callAgvVehicleReBarcodeDtos = callAgvVehicleReBarcodeMapper.findList(map);

        List<CallAgvVehicleBarcodeDTO> callAgvVehicleBarcodeDTOList = new LinkedList<>();
        for (CallAgvVehicleReBarcodeDto callAgvVehicleReBarcodeDto : callAgvVehicleReBarcodeDtos) {
            CallAgvVehicleBarcodeDTO callAgvVehicleBarcodeDTO = new CallAgvVehicleBarcodeDTO();
            callAgvVehicleBarcodeDTO.setVehicleId(callAgvVehicleReBarcodeDto.getVehicleId());
            callAgvVehicleBarcodeDTO.setVehicleCode(callAgvVehicleReBarcodeDto.getVehicleCode());
            callAgvVehicleBarcodeDTO.setVehicleName(callAgvVehicleReBarcodeDto.getVehicleName());

            map.remove("groupByVehicle");
            map.put("vehicleId", callAgvVehicleReBarcodeDto.getVehicleId());
            List<CallAgvVehicleReBarcodeDto> callAgvVehicleReBarcodeDtoList = callAgvVehicleReBarcodeMapper.findList(map);
            callAgvVehicleBarcodeDTO.setCallAgvVehicleReBarcodeDtoList(callAgvVehicleReBarcodeDtoList);
            callAgvVehicleBarcodeDTOList.add(callAgvVehicleBarcodeDTO);
        }

        return callAgvVehicleBarcodeDTOList;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int vehicleDisplacement(Long vehicleId, Long storageTaskPointId, Integer type) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
        searchCallAgvAgvTask.setVehicleId(vehicleId);
        searchCallAgvAgvTask.setTaskStatusList(Arrays.asList(1,2));
        List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
        if (!callAgvAgvTaskDtoList.isEmpty()) {
            throw new BizErrorException("当前货架存在未完成任务，请等待任务结束");
        }
        TemVehicle temVehicle = temVehicleFeignApi.detail(vehicleId).getData();
        if (type == 1 && (StringUtils.isEmpty(temVehicle.getStorageTaskPointId()) || temVehicle.getStorageTaskPointId() == 0)) {
            throw new BizErrorException("当前货架未绑定库位配送点，无法进行移出操作");
        }
        if (type == 2 && StringUtils.isNotEmpty(temVehicle.getStorageTaskPointId()) && temVehicle.getStorageTaskPointId() != 0) {
            throw new BizErrorException("当前货架已绑定库位配送点，无法进行移入操作");
        }
        CallAgvVehicleLog callAgvVehicleLog = new CallAgvVehicleLog();
        callAgvVehicleLog.setVehicleId(vehicleId);

        if (type == 1) {
            callAgvVehicleLog.setStartStorageTaskPointId(temVehicle.getStorageTaskPointId());
            callAgvVehicleLog.setOperatorType((byte) 5);
            callAgvVehicleLog.setRemark("移出货架");

            BaseStorageTaskPoint baseStorageTaskPoint = baseFeignApi.baseStorageTaskPointDetail(temVehicle.getStorageTaskPointId()).getData();
            baseStorageTaskPoint.setStorageTaskPointStatus((byte) 1);
            baseStorageTaskPoint.setModifiedUserId(user.getUserId());
            baseStorageTaskPoint.setModifiedTime(new Date());
            baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPoint);

            temVehicle.setStorageTaskPointId(0l);
            temVehicle.setModifiedUserId(user.getUserId());
            temVehicle.setModifiedTime(new Date());
            temVehicleFeignApi.update(temVehicle);
        } else {
            callAgvVehicleLog.setEndStorageTaskPointId(storageTaskPointId);
            callAgvVehicleLog.setRemark("移入货架");

            BaseStorageTaskPoint baseStorageTaskPoint = baseFeignApi.baseStorageTaskPointDetail(storageTaskPointId).getData();
            if (StringUtils.isEmpty(baseStorageTaskPoint)) {
                throw new BizErrorException("未找到对应的移入库位，请重新扫码");
            }
            if (baseStorageTaskPoint.getStorageTaskPointStatus() == 2) {
                throw new BizErrorException("库位已被占用，请重新扫码");
            }
            baseStorageTaskPoint.setStorageTaskPointStatus((byte) 2);
            baseStorageTaskPoint.setModifiedUserId(user.getUserId());
            baseStorageTaskPoint.setModifiedTime(new Date());
            baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPoint);

            temVehicle.setStorageTaskPointId(storageTaskPointId);
            temVehicle.setModifiedUserId(user.getUserId());
            temVehicle.setModifiedTime(new Date());
            temVehicleFeignApi.update(temVehicle);
        }

        callAgvVehicleLog.setOrgId(user.getOrganizationId());
        callAgvVehicleLog.setCreateUserId(user.getUserId());
        callAgvVehicleLog.setCreateTime(new Date());
        callAgvVehicleLogMapper.insertSelective(callAgvVehicleLog);

        return 1;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int materialTransfer(RequestCallAgvStockDTO requestCallAgvStockDTO) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
        searchCallAgvAgvTask.setVehicleId(requestCallAgvStockDTO.getVehicleId());
        searchCallAgvAgvTask.setTaskStatusList(Arrays.asList(1,2));
        List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
        if (!callAgvAgvTaskDtoList.isEmpty()) {
            throw new BizErrorException("当前货架存在未完成任务，请等待任务结束");
        }
        TemVehicle temVehicle = temVehicleFeignApi.detail(requestCallAgvStockDTO.getVehicleId()).getData();

        Map<String, Object> map = new HashMap<>();
        map.put("barcodeIdList", requestCallAgvStockDTO.getBarcodeIdList());
        List<CallAgvVehicleReBarcodeDto> callAgvVehicleReBarcodeDtoList = findList(map);
        if (!callAgvVehicleReBarcodeDtoList.isEmpty()) {
            List<String> barCodeList = new LinkedList<>();
            for (CallAgvVehicleReBarcodeDto callAgvVehicleReBarcodeDto : callAgvVehicleReBarcodeDtoList) {
                barCodeList.add(callAgvVehicleReBarcodeDto.getBarcode());
            }
            throw new BizErrorException("条码：" + barCodeList.toString() + "已绑定其他货架，不能移入该物料！");
        }

        for (Long id : requestCallAgvStockDTO.getBarcodeIdList()) {
            CallAgvVehicleReBarcode callAgvVehicleReBarcode = new CallAgvVehicleReBarcode();
            callAgvVehicleReBarcode.setBarcodeId(id);
            callAgvVehicleReBarcode.setVehicleId(requestCallAgvStockDTO.getVehicleId());
            callAgvVehicleReBarcode.setOrgId(user.getOrganizationId());
            callAgvVehicleReBarcode.setCreateUserId(user.getUserId());
            callAgvVehicleReBarcode.setCreateTime(new Date());
            callAgvVehicleReBarcodeMapper.insertSelective(callAgvVehicleReBarcode);

            CallAgvVehicleLog callAgvVehicleLog = new CallAgvVehicleLog();
            callAgvVehicleLog.setBarcodeId(id);
            callAgvVehicleLog.setVehicleId(requestCallAgvStockDTO.getVehicleId());
            callAgvVehicleLog.setOperatorType((byte) 1);
            callAgvVehicleLog.setOrgId(user.getOrganizationId());
            callAgvVehicleLog.setCreateUserId(user.getUserId());
            callAgvVehicleLog.setCreateTime(new Date());
            callAgvVehicleLog.setRemark("移入物料");
            callAgvVehicleLogMapper.insertSelective(callAgvVehicleLog);
        }
        temVehicle.setVehicleStatus((byte) 3);
        temVehicle.setModifiedUserId(user.getUserId());
        temVehicle.setModifiedTime(new Date());
        temVehicleFeignApi.update(temVehicle);

        return 1;
    }

    @Override
    public List<CallAgvWarehouseAreaMaterialDto> agvWarehouseAreaMaterialSummary(SearchCallAgvStorageMaterial searchCallAgvStorageMaterial) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        searchCallAgvStorageMaterial.setSummary(1);
        searchCallAgvStorageMaterial.setOrgId(user.getOrganizationId());
        List<CallAgvStorageMaterialDto> callAgvStorageMaterialDtoList = callAgvVehicleReBarcodeMapper.callAgvStorageMaterialList(searchCallAgvStorageMaterial);
        List<CallAgvWarehouseAreaMaterialDto> callAgvWarehouseAreaMaterialDtoList = new ArrayList<>();
        for (CallAgvStorageMaterialDto callAgvStorageMaterialDto : callAgvStorageMaterialDtoList) {
            CallAgvWarehouseAreaMaterialDto callAgvWarehouseAreaMaterialDto = new CallAgvWarehouseAreaMaterialDto();
            BeanUtils.autoFillEqFields(callAgvStorageMaterialDto, callAgvWarehouseAreaMaterialDto);
            callAgvWarehouseAreaMaterialDtoList.add(callAgvWarehouseAreaMaterialDto);
        }

        return callAgvWarehouseAreaMaterialDtoList;
    }

    @Override
    public List<CallAgvStorageMaterialDto> agvStorageMaterialDetail(SearchCallAgvStorageMaterial searchCallAgvStorageMaterial) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        searchCallAgvStorageMaterial.setOrgId(user.getOrganizationId());
        return callAgvVehicleReBarcodeMapper.callAgvStorageMaterialList(searchCallAgvStorageMaterial);
    }

    @Override
    @Transactional
    @LcnTransaction
    public int barcodeInWarehouseArea(BarcodeWarehouseAreaDto barcodeWarehouseAreaDto) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<CallAgvWarehouseAreaReBarcode> callAgvWarehouseAreaReBarcodeList = new LinkedList<>();
        List<CallAgvWarehouseAreaBarcodeLog> callAgvWarehouseAreaBarcodeLogList = new LinkedList<>();
        List<GoodsDetail> goodsDetails = new LinkedList<>();
        String warehouseCode = "";
        String warehouseaAreaCode = "";
        for (Long barcodeId : barcodeWarehouseAreaDto.getBarcodeIdList()) {
            CallAgvBarcode callAgvBarcode = callAgvBarcodeMapper.selectByPrimaryKey(barcodeId);
            if (callAgvBarcode.getBarcodeStatus() == 2 && callAgvBarcode.getBarcodeStatus() == 3) {
                throw new BizErrorException("条码：" + callAgvBarcode.getBarcode() + "已备料或者已入库，不能再入库！");
            }
            CallAgvWarehouseAreaReBarcode callAgvWarehouseAreaReBarcode = new CallAgvWarehouseAreaReBarcode();
            callAgvWarehouseAreaReBarcode.setBarcodeId(barcodeId);
            callAgvWarehouseAreaReBarcode.setWarehouseAreaId(barcodeWarehouseAreaDto.getWarehouseAreaId());
            callAgvWarehouseAreaReBarcode.setStatus((byte) 1);
            callAgvWarehouseAreaReBarcode.setOrgId(user.getOrganizationId());
            callAgvWarehouseAreaReBarcode.setCreateTime(new Date());
            callAgvWarehouseAreaReBarcode.setCreateUserId(user.getUserId());
            callAgvWarehouseAreaReBarcode.setIsDelete((byte) 1);
            callAgvWarehouseAreaReBarcodeList.add(callAgvWarehouseAreaReBarcode);

            CallAgvWarehouseAreaBarcodeLog callAgvWarehouseAreaBarcodeLog = new CallAgvWarehouseAreaBarcodeLog();
            callAgvWarehouseAreaBarcodeLog.setBarcodeId(barcodeId);
            callAgvWarehouseAreaBarcodeLog.setWarehouseAreaId(barcodeWarehouseAreaDto.getWarehouseAreaId());
            callAgvWarehouseAreaBarcodeLog.setOperatorType((byte) 1);
            callAgvWarehouseAreaBarcodeLog.setOperatorTime(new Date());
            callAgvWarehouseAreaBarcodeLog.setOperatorUserId(user.getUserId());
            callAgvWarehouseAreaBarcodeLog.setStatus((byte) 1);
            callAgvWarehouseAreaBarcodeLog.setOrgId(user.getOrganizationId());
            callAgvWarehouseAreaBarcodeLog.setCreateTime(new Date());
            callAgvWarehouseAreaBarcodeLog.setCreateUserId(user.getUserId());
            callAgvWarehouseAreaBarcodeLog.setIsDelete((byte) 1);
            callAgvWarehouseAreaBarcodeLogList.add(callAgvWarehouseAreaBarcodeLog);

            callAgvBarcode.setBarcodeStatus((byte) 3);
            callAgvBarcode.setModifiedUserId(user.getUserId());
            callAgvBarcode.setModifiedTime(new Date());
            callAgvBarcodeMapper.updateByPrimaryKeySelective(callAgvBarcode);

            GoodsDetail goodsDetail = new GoodsDetail();
            goodsDetail.setBatchNumber(callAgvBarcode.getBatch());
            goodsDetail.setBarCode(callAgvBarcode.getBarcode());
            goodsDetail.setActualQuantity(callAgvBarcode.getQty().doubleValue());
            goodsDetail.setMaterialCodes(callAgvBarcode.getMaterialCode());
            goodsDetails.add(goodsDetail);

            if (StringUtils.isEmpty(warehouseaAreaCode, warehouseCode)) {
                SearchBaseWarehouseArea searchBaseWarehouseArea = new SearchBaseWarehouseArea();
                searchBaseWarehouseArea.setWarehouseAreaId(callAgvWarehouseAreaReBarcode.getWarehouseAreaId());
                List<BaseWarehouseAreaDto> baseWarehouseAreaDtoList = baseFeignApi.findWarehouseAreaList(searchBaseWarehouseArea).getData();
                warehouseaAreaCode = baseWarehouseAreaDtoList.get(0).getWarehouseAreaCode();
                warehouseCode = baseWarehouseAreaDtoList.get(0).getWarehouseCode();
            }
        }
        callAgvWarehouseAreaReBarcodeMapper.insertList(callAgvWarehouseAreaReBarcodeList);
        callAgvWarehouseAreaBarcodeLogMapper.insertList(callAgvWarehouseAreaBarcodeLogList);
        // 扫码入库，回传数据到SCM
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("warehouseCode", warehouseCode);
        jsonMap.put("warehouseaAreaCode", warehouseaAreaCode);
        jsonMap.put("pkGroup", pkGroup);
        jsonMap.put("pkOrg", pkOrg);
        jsonMap.put("factoryCode", factoryCode);
        jsonMap.put("goodsDetails", JSONObject.toJSONString(goodsDetails));
        log.info("扫码入库参数：" + JSONObject.toJSONString(jsonMap));
        Map<String, Object> paramMap = scmParam(jsonMap, "mls.scanCode.in.warehouse");
        log.info("扫码入库，回传数据到SCM : " + JsonUtils.objectToJson(paramMap));
        String result = RestTemplateUtil.postForString(url, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        log.info("扫码入库，回传数据到SCM : " + jsonObject);
        if (!"0".equals(jsonObject.get("code"))) {
            throw new BizErrorException("扫码入库失败：" + jsonObject.get("msg"));
        }

        return 1;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int barcodeOutWarehouseArea(List<Long> warehouseAreaReBarcodeIdList) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<CallAgvWarehouseAreaBarcodeLog> callAgvWarehouseAreaBarcodeLogList = new LinkedList<>();
        List<GoodsDetail> goodsDetails = new LinkedList<>();
        String warehouseCode = "";
        String warehouseaAreaCode = "";
        for (Long warehouseAreaReBarcodeId : warehouseAreaReBarcodeIdList) {
            CallAgvWarehouseAreaReBarcode callAgvWarehouseAreaReBarcode = callAgvWarehouseAreaReBarcodeMapper.selectByPrimaryKey(warehouseAreaReBarcodeId);
            CallAgvWarehouseAreaBarcodeLog callAgvWarehouseAreaBarcodeLog = new CallAgvWarehouseAreaBarcodeLog();
            callAgvWarehouseAreaBarcodeLog.setBarcodeId(callAgvWarehouseAreaReBarcode.getBarcodeId());
            callAgvWarehouseAreaBarcodeLog.setWarehouseAreaId(callAgvWarehouseAreaReBarcode.getWarehouseAreaId());
            callAgvWarehouseAreaBarcodeLog.setOperatorType((byte) 2);
            callAgvWarehouseAreaBarcodeLog.setOperatorTime(new Date());
            callAgvWarehouseAreaBarcodeLog.setOperatorUserId(user.getUserId());
            callAgvWarehouseAreaBarcodeLog.setStatus((byte) 1);
            callAgvWarehouseAreaBarcodeLog.setOrgId(user.getOrganizationId());
            callAgvWarehouseAreaBarcodeLog.setCreateTime(new Date());
            callAgvWarehouseAreaBarcodeLog.setCreateUserId(user.getUserId());
            callAgvWarehouseAreaBarcodeLog.setIsDelete((byte) 1);
            callAgvWarehouseAreaBarcodeLogList.add(callAgvWarehouseAreaBarcodeLog);

            CallAgvBarcode callAgvBarcode = callAgvBarcodeMapper.selectByPrimaryKey(callAgvWarehouseAreaReBarcode.getBarcodeId());
            callAgvBarcode.setBarcodeStatus((byte) 3);
            callAgvBarcode.setModifiedUserId(user.getUserId());
            callAgvBarcode.setModifiedTime(new Date());
            callAgvBarcodeMapper.updateByPrimaryKeySelective(callAgvBarcode);

            GoodsDetail goodsDetail = new GoodsDetail();
            goodsDetail.setBatchNumber(callAgvBarcode.getBatch());
            goodsDetail.setBarCode(callAgvBarcode.getBarcode());
            goodsDetail.setActualQuantity(callAgvBarcode.getQty().doubleValue());
            goodsDetail.setMaterialCodes(callAgvBarcode.getMaterialCode());
            goodsDetails.add(goodsDetail);

            if (StringUtils.isEmpty(warehouseaAreaCode, warehouseCode)) {
                SearchBaseWarehouseArea searchBaseWarehouseArea = new SearchBaseWarehouseArea();
                searchBaseWarehouseArea.setWarehouseAreaId(callAgvWarehouseAreaReBarcode.getWarehouseAreaId());
                List<BaseWarehouseAreaDto> baseWarehouseAreaDtoList = baseFeignApi.findWarehouseAreaList(searchBaseWarehouseArea).getData();
                warehouseaAreaCode = baseWarehouseAreaDtoList.get(0).getWarehouseAreaCode();
                warehouseCode = baseWarehouseAreaDtoList.get(0).getWarehouseCode();
            }
        }
        // 扫码出库，回传数据到SCM
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("warehouseCode", warehouseCode);
        jsonMap.put("warehouseaAreaCode", warehouseaAreaCode);
        jsonMap.put("pkGroup", pkGroup);
        jsonMap.put("pkOrg", pkOrg);
        jsonMap.put("factoryCode", factoryCode);
        jsonMap.put("goodsDetails", JSONObject.toJSONString(goodsDetails));
        log.info("扫码出库参数：" + JSONObject.toJSONString(jsonMap));
        Map<String, Object> paramMap = scmParam(jsonMap, "mls.scanCode.out.warehouse");
        log.info("扫码出库，回传数据到SCM : " + JsonUtils.objectToJson(paramMap));
        String result = RestTemplateUtil.postForString(url, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        log.info("扫码出库，回传数据到SCM : " + jsonObject);
        if (!"0".equals(jsonObject.get("code"))) {
            throw new BizErrorException("扫码出库失败：" + jsonObject.get("msg"));
        }

        callAgvWarehouseAreaBarcodeLogMapper.insertList(callAgvWarehouseAreaBarcodeLogList);
        callAgvWarehouseAreaReBarcodeMapper.deleteByIds(org.apache.commons.lang3.StringUtils.join(warehouseAreaReBarcodeIdList, ","));

        return 1;
    }

    /**
     * @param taskTyp          任务类型
     * @param positionCodeList 坐标列表
     * @param podCode          货架编号
     * @return agv任务单号
     * @throws BizErrorException
     */
    @Override
    public String genAgvSchedulingTask(String taskTyp, List<String> positionCodeList, String podCode) throws Exception {

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("taskTyp", taskTyp);
        List<PositionCodePath> positionCodePathList = new LinkedList<>();
        for (String positionCode : positionCodeList) {
            // 起始地标条码
            PositionCodePath positionCodePath = new PositionCodePath();
            positionCodePath.setPositionCode(positionCode);
            positionCodePath.setType("00");
            positionCodePathList.add(positionCodePath);
        }
        dataMap.put("positionCodePath", positionCodePathList);
        dataMap.put("podCode", podCode);

        String AGVResult = agvFeignApi.genAgvSchedulingTask(dataMap).getData();
        log.info("启动AGV任务：param : " + JSONObject.toJSONString(dataMap) + " ;response : " + AGVResult + "\n\r");

        RcsResponseDTO rcsResponseDTO = BeanUtils.convertJson(AGVResult, new TypeToken<RcsResponseDTO>() {
        }.getType());
        String taskCode = rcsResponseDTO.getData();
        if (!"0".equals(rcsResponseDTO.getCode())) {
            throw new BizErrorException("请求AGV失败：" + AGVResult);
        }

        return taskCode;
    }

    /**
     *
     * @param baseStorageTaskPoint
     * @param temVehicle
     * @param user
     * @param goodsDetails
     * @return
     * @throws Exception
     */
    private Map<String, Object> productionReportParam(BaseStorageTaskPoint baseStorageTaskPoint, TemVehicle temVehicle, SysUser user, List<GoodsDetail> goodsDetails) throws Exception {

        MlsSaveProductionReport mlsSaveProductionReport = new MlsSaveProductionReport();
        mlsSaveProductionReport.setInStorageCode(baseStorageTaskPoint.getStorageCode());
        mlsSaveProductionReport.setContainCode(temVehicle.getVehicleCode());
        mlsSaveProductionReport.setWarehouseAreaCode(baseStorageTaskPoint.getWarehouseAreaCode());

        SearchHrUserInfo searchHrUserInfo = new SearchHrUserInfo();
//        searchHrUserInfo.setJobNum(user.getUserCode());
//        List<HrUserInfoDto> hrUserInfoDtoList = mulinsenFeignApi.findList(searchHrUserInfo).getData();
//        if (hrUserInfoDtoList.isEmpty() || StringUtils.isEmpty(hrUserInfoDtoList.get(0).getErpDeptcode())) {
//            throw new BizErrorException("没有找到当前用户的部门，请检查ERP用户和部门的关联数据");
//        }
//        mlsSaveProductionReport.setDeptCodes(hrUserInfoDtoList.get(0).getErpDeptcode());
        mlsSaveProductionReport.setFactoryCode(factoryCode);
        mlsSaveProductionReport.setCreateUserName("0504291");
        mlsSaveProductionReport.setPkGroup(pkGroup);
        mlsSaveProductionReport.setPkOrg(pkOrg);
        mlsSaveProductionReport.setUserName("0504291");
        mlsSaveProductionReport.setWarehouseCode(baseStorageTaskPoint.getWarehouseCode());
        mlsSaveProductionReport.setGoodsDetails(goodsDetails);
        log.info("生产报告参数：" + JSONObject.toJSONString(mlsSaveProductionReport));

        return scmParam(mlsSaveProductionReport, "mls.save.production.report");
    }

    /**
     *
     * @param outBaseStorageTaskPoint
     * @param inBaseStorageTaskPoint
     * @param user
     * @param goodsDetails
     * @return
     * @throws Exception
     */
    private Map<String, Object> outWarehouse(BaseStorageTaskPoint outBaseStorageTaskPoint, BaseStorageTaskPoint inBaseStorageTaskPoint, SysUser user, List<GoodsDetail> goodsDetails) throws Exception {
        MlsSaveOutWarehouse mlsSaveOutWarehouse = new MlsSaveOutWarehouse();
        mlsSaveOutWarehouse.setOutWarehouseCode(outBaseStorageTaskPoint.getWarehouseCode());
        mlsSaveOutWarehouse.setInStorageCode(inBaseStorageTaskPoint.getStorageCode());
        mlsSaveOutWarehouse.setInWarehouseArea(inBaseStorageTaskPoint.getWarehouseAreaCode());
        mlsSaveOutWarehouse.setOutStorageCode(outBaseStorageTaskPoint.getStorageCode());

        SearchHrUserInfo searchHrUserInfo = new SearchHrUserInfo();
//        searchHrUserInfo.setJobNum(user.getUserCode());
//        List<HrUserInfoDto> hrUserInfoDtoList = mulinsenFeignApi.findList(searchHrUserInfo).getData();
//        if (hrUserInfoDtoList.isEmpty() || StringUtils.isEmpty(hrUserInfoDtoList.get(0).getErpDeptcode())) {
//            throw new BizErrorException("没有找到当前用户的部门，请检查ERP用户和部门的关联数据");
//        }
//        mlsSaveOutWarehouse.setDeptCodes(hrUserInfoDtoList.get(0).getErpDeptcode());
        mlsSaveOutWarehouse.setFactoryCode(factoryCode);
        mlsSaveOutWarehouse.setCreateUserName("0504291");
        mlsSaveOutWarehouse.setPkGroup(pkGroup);
        mlsSaveOutWarehouse.setPkOrg(pkOrg);
        mlsSaveOutWarehouse.setUserName("0504291");
        mlsSaveOutWarehouse.setWarehouseCode(inBaseStorageTaskPoint.getWarehouseCode());
        mlsSaveOutWarehouse.setGoodsDetails(goodsDetails);
        log.info("叫料出库参数：" + JSONObject.toJSONString(mlsSaveOutWarehouse));

        return scmParam(mlsSaveOutWarehouse, "mls.save.out.warehouse");
    }

    public Map<String, Object> scmParam(Object object, String name) throws Exception {
        String json = JsonUtils.objectToJson(object);
        json = URLEncoder.encode(json, "utf-8");

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("data", json);
        map.put("app_key", appKey);
        map.put("version", "");
        String sign = scmSign.buildSign(map, secret);
        map.put("sign", sign);
        map.put("timestamp", SCMSign.getTime());

        return map;
    }
}
