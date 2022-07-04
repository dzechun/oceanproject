package com.fantechs.provider.guest.callagv.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.fantechs.common.base.agv.dto.PositionCodePath;
import com.fantechs.common.base.agv.dto.RcsResponseDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.*;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import com.fantechs.common.base.general.entity.callagv.CallAgvProductionInLog;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleLog;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleReBarcode;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.agv.AgvFeignApi;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.guest.callagv.mapper.CallAgvAgvTaskMapper;
import com.fantechs.provider.guest.callagv.mapper.CallAgvProductionInLogMapper;
import com.fantechs.provider.guest.callagv.mapper.CallAgvVehicleLogMapper;
import com.fantechs.provider.guest.callagv.mapper.CallAgvVehicleReBarcodeMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleReBarcodeService;
import com.google.gson.reflect.TypeToken;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
    private TemVehicleFeignApi temVehicleFeignApi;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    private AgvFeignApi agvFeignApi;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<CallAgvVehicleReBarcodeDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return callAgvVehicleReBarcodeMapper.findList(map);
    }

    @Override
    @Transactional
    @GlobalTransactional
    public int callAgvStock(RequestCallAgvStockDTO requestCallAgvStockDTO) throws BizErrorException {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        TemVehicle temVehicle = temVehicleFeignApi.detail(requestCallAgvStockDTO.getVehicleId()).getData();
        if (temVehicle.getStatus() != 1) {
            throw new BizErrorException("货架不是空闲状态，不能进行备料！");
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
            callAgvVehicleLog.setStartStorageTaskPointId(requestCallAgvStockDTO.getStorageTaskPointId());
            callAgvVehicleLog.setOperatorType((byte) 1);
            callAgvVehicleLog.setOrgId(user.getOrganizationId());
            callAgvVehicleLog.setCreateUserId(user.getUserId());
            callAgvVehicleLog.setCreateTime(new Date());
            callAgvVehicleLogMapper.insertSelective(callAgvVehicleLog);
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

        return requestCallAgvStockDTO.getBarcodeIdList().size();
    }

    @Override
    @Transactional
    @GlobalTransactional
    public String callAgvDistribution(Long vehicleId, Long warehouseAreaId, Long storageTaskPointId, Integer type) throws Exception {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
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
        BaseStorageTaskPoint baseStorageTaskPoint = baseFeignApi.baseStorageTaskPointDetail(temVehicle.getStorageTaskPointId()).getData();
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
            baseStorageTaskPointEnd = baseFeignApi.baseStorageTaskPointDetail(storageTaskPointId).getData();
            if (baseStorageTaskPointEnd.getStorageTaskPointStatus() != 1) {
                throw new BizErrorException("目的库位对应的配送点被占用，请重新选择");
            }
        }

        temVehicle.setStorageTaskPointId(0l);
        temVehicle.setModifiedUserId(user.getUserId());
        temVehicle.setModifiedTime(new Date());
        temVehicleFeignApi.update(temVehicle);

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
            }
        }

        // 记录AGV任务
        CallAgvAgvTask callAgvAgvTask = new CallAgvAgvTask();
        callAgvAgvTask.setVehicleId(vehicleId);
        callAgvAgvTask.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
        callAgvAgvTask.setEndStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
        callAgvAgvTask.setTaskStatus((byte) 2);
        callAgvAgvTask.setOperateType(type.byteValue());
        callAgvAgvTask.setOrgId(user.getOrganizationId());
        callAgvAgvTask.setCreateUserId(user.getUserId());

        String message = "";
        String taskCode = "";
        try {
            if (type == 1) {
                message = "备料配送";
            } else if (type == 2) {
                message = "叫料配送";
            } else {
                message = "空货架返回";
            }
            Boolean taskBoolean = false;
            String taskTyp = temVehicle.getAgvTaskTemplate();
            if (StringUtils.isNotEmpty(baseStorageTaskPoint.getType(),
                    baseStorageTaskPointEnd.getType())
                    && !baseStorageTaskPoint.getType().equals(baseStorageTaskPointEnd.getType())) {
                taskTyp = temVehicle.getAgvTaskTemplateSecond();

                taskBoolean = true;
            }

            if (taskBoolean && StringUtils.isNotEmpty(redisUtil.get(baseStorageTaskPoint.getType()))) {
                baseStorageTaskPoint.setStorageTaskPointStatus((byte) 1);
                baseStorageTaskPoint.setModifiedUserId(user.getUserId());

                temVehicle.setStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
                temVehicle.setModifiedUserId(user.getUserId());
                temVehicle.setRemark("电梯任务等待队列中");
                List<GenAgvSchedulingTaskDTO> genAgvSchedulingTaskDTOList = BeanUtils.convertJson(redisUtil.get(baseStorageTaskPoint.getType()).toString(), new TypeToken<List<GenAgvSchedulingTaskDTO>>() {
                }.getType());
                GenAgvSchedulingTaskDTO genAgvSchedulingTaskDTO = new GenAgvSchedulingTaskDTO();
                genAgvSchedulingTaskDTO.setTaskTyp(taskTyp);
                genAgvSchedulingTaskDTO.setPositionCodeList(positionCodeList);
                genAgvSchedulingTaskDTO.setPodCode(temVehicle.getVehicleCode());
                genAgvSchedulingTaskDTO.setBaseStorageTaskPoint(baseStorageTaskPoint);
                genAgvSchedulingTaskDTO.setTemVehicle(temVehicle);
                genAgvSchedulingTaskDTO.setCallAgvAgvTask(callAgvAgvTask);
                genAgvSchedulingTaskDTOList.add(genAgvSchedulingTaskDTO);
                redisUtil.set(baseStorageTaskPoint.getType(), JSONObject.toJSONString(genAgvSchedulingTaskDTOList));
                log.info("==========记录AGV电梯任务队列 : key " + baseStorageTaskPoint.getType() + " , value : " + JSONObject.toJSONString(genAgvSchedulingTaskDTOList));
            } else {
                taskCode = genAgvSchedulingTask(taskTyp, positionCodeList, temVehicle.getVehicleCode());

                callAgvAgvTask.setTaskCode(taskCode);
                callAgvAgvTask.setCreateTime(new Date());
                callAgvAgvTaskMapper.insertSelective(callAgvAgvTask);

                log.info("==========启动agv执行" + message + "作业任务==============\r\n");
                baseStorageTaskPoint.setStorageTaskPointStatus((byte) 1);
                baseStorageTaskPoint.setModifiedUserId(user.getUserId());
                redisUtil.set("3-" + taskCode, JSONObject.toJSONString(baseStorageTaskPoint));
                log.info("=========记录当前" + message + "作业对应的起始配送点 : key : " + "3-" + taskCode + " value : " + JSONObject.toJSONString(baseStorageTaskPoint) + "\r\n");

                temVehicle.setStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
                temVehicle.setModifiedUserId(user.getUserId());
                temVehicle.setRemark(taskCode);
                redisUtil.set("2-" + taskCode, JSONObject.toJSONString(temVehicle));
                log.info("=========记录当前" + message + "作业载具对应的目的配送点 : key : " + "2-" + taskCode + " value : " + JSONObject.toJSONString(temVehicle) + "\r\n");

                if (taskBoolean) {
                    redisUtil.set("4-" + taskCode, baseStorageTaskPoint.getType());
                    log.info("=========记录AGV电梯任务作业对应的配送方式 : key : " + "4-" + taskCode + " value : " + baseStorageTaskPoint.getType() + "\r\n");
                    List<GenAgvSchedulingTaskDTO> genAgvSchedulingTaskDTOList = new ArrayList<>();
                    GenAgvSchedulingTaskDTO genAgvSchedulingTaskDTO = new GenAgvSchedulingTaskDTO();
                    genAgvSchedulingTaskDTO.setTaskTyp(taskTyp);
                    genAgvSchedulingTaskDTO.setPositionCodeList(positionCodeList);
                    genAgvSchedulingTaskDTO.setPodCode(temVehicle.getVehicleCode());
                    genAgvSchedulingTaskDTO.setBaseStorageTaskPoint(baseStorageTaskPoint);
                    genAgvSchedulingTaskDTO.setTemVehicle(temVehicle);
                    genAgvSchedulingTaskDTOList.add(genAgvSchedulingTaskDTO);
                    redisUtil.set(baseStorageTaskPoint.getType(), JSONObject.toJSONString(genAgvSchedulingTaskDTOList));
                    log.info("==========记录AGV电梯任务队列 : key " + baseStorageTaskPoint.getType() + " , value : " + JSONObject.toJSONString(genAgvSchedulingTaskDTOList));
                }
            }
        } catch (BizErrorException e) {
            throw new BizErrorException("启动agv执行" + message + "作业任务失败" + e.getMessage());
        }

        return taskCode;
    }

    @Override
    @Transactional
    @GlobalTransactional
    public int vehicleBarcodeUnbound(RequestBarcodeUnboundDTO requestBarcodeUnboundDTO) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        TemVehicle temVehicle = temVehicleFeignApi.detail(requestBarcodeUnboundDTO.getVehicleId()).getData();
        if (requestBarcodeUnboundDTO.getType() == 1) {
            temVehicle.setVehicleStatus((byte) 1);
            temVehicle.setVehicleId(requestBarcodeUnboundDTO.getVehicleId());
            temVehicle.setModifiedUserId(user.getUserId());
            temVehicle.setModifiedTime(new Date());
            temVehicleFeignApi.update(temVehicle);
        }

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
        }

        Example example = new Example(CallAgvVehicleReBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("vehicleReBarcodeId", requestBarcodeUnboundDTO.getVehicleReBarcodeIdList());
        callAgvVehicleReBarcodeMapper.deleteByExample(example);

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
}
