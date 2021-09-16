package com.fantechs.provider.guest.callagv.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.agv.dto.PositionCodePath;
import com.fantechs.common.base.agv.dto.RcsResponseDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleBarcodeDTO;
import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleReBarcodeDto;
import com.fantechs.common.base.general.dto.callagv.RequestBarcodeUnboundDTO;
import com.fantechs.common.base.general.dto.callagv.RequestCallAgvStockDTO;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleLog;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleReBarcode;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.agv.AgvFeignApi;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.guest.callagv.mapper.CallAgvVehicleLogMapper;
import com.fantechs.provider.guest.callagv.mapper.CallAgvVehicleReBarcodeMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleReBarcodeService;
import com.google.gson.reflect.TypeToken;
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
    @LcnTransaction
    public int callAgvStock(RequestCallAgvStockDTO requestCallAgvStockDTO) throws BizErrorException {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        TemVehicle temVehicle = temVehicleFeignApi.detail(requestCallAgvStockDTO.getVehicleId()).getData();
        if (temVehicle.getStatus() != 1) {
            throw new BizErrorException("货架不是空闲状态，不能进行备料！");
        }
        BaseStorageTaskPoint baseStorageTaskPoint = baseFeignApi.baseStorageTaskPointDetail(requestCallAgvStockDTO.getStorageTaskPointId()).getData();
        if (baseStorageTaskPoint.getStorageTaskPointStatus() != 1) {
            throw new BizErrorException("当前库位配送点不是空闲状态，不能进行备料！");
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
    @LcnTransaction
    public String callAgvDistribution(Long vehicleId, Long warehouseAreaId, Integer type) throws BizErrorException {
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

        String taskCode = CodeUtils.getId("CAD-");

        temVehicle.setStorageTaskPointId(0l);
        temVehicle.setModifiedUserId(user.getUserId());
        temVehicle.setModifiedTime(new Date());
        temVehicle.setRemark(taskCode);
        temVehicleFeignApi.update(temVehicle);

        BaseStorageTaskPoint baseStorageTaskPointUpdate = baseStorageTaskPointList.get(0);
        baseStorageTaskPointUpdate.setStorageTaskPointStatus((byte) 2);
        baseStorageTaskPointUpdate.setModifiedUserId(user.getUserId());
        baseStorageTaskPointUpdate.setModifiedTime(new Date());
        baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPointUpdate);

        if (type == 2 || type == 3) {
            CallAgvVehicleLog callAgvVehicleLog = new CallAgvVehicleLog();
            callAgvVehicleLog.setVehicleId(vehicleId);
            callAgvVehicleLog.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
            callAgvVehicleLog.setEndStorageTaskPointId(baseStorageTaskPointList.get(0).getStorageTaskPointId());
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

        String message = "";
        try {
            if (type == 1) {
                message = "备料配送";
            } else if (type == 2) {
                message = "叫料配送";
            } else {
                message = "空货架返回";
            }
//            taskCode = genAgvSchedulingTask(temVehicle.getAgvTaskTemplate(), positionCodeList);
            log.info("==========启动agv执行" + message + "作业任务==============\r\n");
            baseStorageTaskPoint.setStorageTaskPointStatus((byte) 1);
            baseStorageTaskPoint.setModifiedUserId(user.getUserId());
            redisUtil.set("3-" + taskCode, JSONObject.toJSONString(baseStorageTaskPoint));
            log.info("=========记录当前" + message + "作业对应的起始配送点 : key : " + "3-" + taskCode + " value : " + JSONObject.toJSONString(baseStorageTaskPoint) + "\r\n");

            temVehicle.setStorageTaskPointId(baseStorageTaskPointList.get(0).getStorageTaskPointId());
            temVehicle.setModifiedUserId(user.getUserId());
            redisUtil.set("2-" + taskCode, JSONObject.toJSONString(temVehicle));
            log.info("=========记录当前" + message + "作业载具对应的目的配送点 : key : " + "2-" + taskCode + " value : " + JSONObject.toJSONString(temVehicle) + "\r\n");
        } catch (BizErrorException e) {
            throw new BizErrorException("启动agv执行" + message + "作业任务失败" + e.getMessage());
        }

        return taskCode;
    }

    @Override
    @Transactional
    @LcnTransaction
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
            List<CallAgvVehicleReBarcodeDto> callAgvVehicleReBarcodeDtoList = callAgvVehicleReBarcodeMapper.findList(map);
            callAgvVehicleBarcodeDTO.setCallAgvVehicleReBarcodeDtoList(callAgvVehicleReBarcodeDtoList);
            callAgvVehicleBarcodeDTOList.add(callAgvVehicleBarcodeDTO);
        }

        return callAgvVehicleBarcodeDTOList;
    }

    /**
     * @param taskTyp          任务类型
     * @param positionCodeList 坐标列表
     * @return agv任务单号
     * @throws BizErrorException
     */
    @Override
    public String genAgvSchedulingTask(String taskTyp, List<String> positionCodeList) throws Exception {

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
