package com.fantechs.provider.guest.callagv.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.agv.dto.AgvCallBackDTO;
import com.fantechs.common.base.agv.dto.WarnCallbackDTO;
import com.fantechs.common.base.agv.dto.WarnCallbackData;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvAgvTaskDto;
import com.fantechs.common.base.general.dto.callagv.CallAgvDistributionDto;
import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTaskBarcode;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleReBarcode;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvAgvTask;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.general.entity.tem.search.SearchTemVehicle;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.guest.callagv.mapper.CallAgvAgvTaskBarcodeMapper;
import com.fantechs.provider.guest.callagv.mapper.CallAgvAgvTaskMapper;
import com.fantechs.provider.guest.callagv.mapper.CallAgvVehicleReBarcodeMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvAgvTaskService;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleReBarcodeService;
import com.fantechs.provider.guest.callagv.service.RcsCallBackService;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class RcsCallBackServiceImpl implements RcsCallBackService {

    @Resource
    private TemVehicleFeignApi temVehicleFeignApi;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    RedisUtil redisUtil;

    @Resource
    private CallAgvVehicleReBarcodeService callAgvVehicleReBarcodeService;

    @Resource
    private CallAgvAgvTaskService callAgvAgvTaskService;

    @Resource
    private CallAgvAgvTaskMapper callAgvAgvTaskMapper;

    @Resource
    private CallAgvVehicleReBarcodeMapper callAgvVehicleReBarcodeMapper;

    @Resource
    private CallAgvAgvTaskBarcodeMapper callAgvAgvTaskBarcodeMapper;

    @Value("${SCM.url}")
    private String url;

    @Override
    @Transactional
    @LcnTransaction
    public String agvCallback(AgvCallBackDTO agvCallBackDTO) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        if (StringUtils.isEmpty(
                agvCallBackDTO.getTaskCode(),
                agvCallBackDTO.getPodCode())) {

            throw new Exception("通知MES失败，没有找到任务单号或货架编号！");
        }
//        if (agvCallBackDTO.getRobotCode().equals("-1")) {
//            throw new BizErrorException("没有找到对应的AGV编号：" + agvCallBackDTO.getRobotCode());
//        }

        // RCS通知货架转移
        if (Integer.valueOf(agvCallBackDTO.getMethod()) > 4) {
            if (Integer.valueOf(agvCallBackDTO.getMethod()) == 7) {
                SearchTemVehicle searchTemVehicle = new SearchTemVehicle();
                searchTemVehicle.setVehicleCode(agvCallBackDTO.getPodCode());
                List<TemVehicleDto> temVehicleDtoList = temVehicleFeignApi.findList(searchTemVehicle).getData();
                // 记录AGV货架转移任务
                CallAgvAgvTask callAgvAgvTask = new CallAgvAgvTask();
                callAgvAgvTask.setTaskCode(agvCallBackDTO.getTaskCode());
                callAgvAgvTask.setVehicleId(temVehicleDtoList.get(0).getVehicleId());
                callAgvAgvTask.setTaskStatus((byte) 2);
                callAgvAgvTask.setOperateType((byte) 4);
                callAgvAgvTask.setStatus((byte) 1);
                callAgvAgvTask.setOrgId(user.getOrganizationId());
                callAgvAgvTask.setIsDelete((byte) 1);
                callAgvAgvTask.setCreateUserId(user.getUserId());
                callAgvAgvTask.setCreateTime(new Date());
                callAgvAgvTaskMapper.insertUseGeneratedKeys(callAgvAgvTask);

                Example example = new Example(CallAgvVehicleReBarcode.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("vehicleId", temVehicleDtoList.get(0).getVehicleId()).andEqualTo("orgId", user.getOrganizationId());
                List<CallAgvVehicleReBarcode> callAgvVehicleReBarcodeList = callAgvVehicleReBarcodeMapper.selectByExample(example);

                if (!callAgvVehicleReBarcodeList.isEmpty()) {
                    List<CallAgvAgvTaskBarcode> callAgvAgvTaskBarcodeList = new LinkedList<>();
                    for (CallAgvVehicleReBarcode callAgvVehicleReBarcode : callAgvVehicleReBarcodeList) {
                        CallAgvAgvTaskBarcode callAgvAgvTaskBarcode = new CallAgvAgvTaskBarcode();
                        callAgvAgvTaskBarcode.setAgvTaskId(callAgvAgvTask.getAgvTaskId());
                        callAgvAgvTaskBarcode.setBarcodeId(callAgvVehicleReBarcode.getBarcodeId());
                        callAgvAgvTaskBarcode.setStatus((byte) 1);
                        callAgvAgvTaskBarcode.setOrgId(user.getOrganizationId());
                        callAgvAgvTaskBarcode.setCreateTime(new Date());
                        callAgvAgvTaskBarcodeList.add(callAgvAgvTaskBarcode);
                    }
                    callAgvAgvTaskBarcodeMapper.insertList(callAgvAgvTaskBarcodeList);
                }

                return "0";
            }
            SearchBaseStorageTaskPoint searchBaseStorageTaskPoint = new SearchBaseStorageTaskPoint();
            searchBaseStorageTaskPoint.setXyzCode(agvCallBackDTO.getCurrentPositionCode());
            List<BaseStorageTaskPoint> baseStorageTaskPointList = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData();
            if (baseStorageTaskPointList.isEmpty()) {
                throw new BizErrorException("货架转移任务 : " + agvCallBackDTO.getTaskCode() + " 没有找到对应的配送点：" + agvCallBackDTO.getCurrentPositionCode());
            }
            if ("5".equals(agvCallBackDTO.getMethod()) && StringUtils.isEmpty(baseStorageTaskPointList.get(0).getVehicleId())) {
                throw new BizErrorException("货架转移任务 : " + agvCallBackDTO.getTaskCode() + " 没有找到对应的配送点绑定的货架");
            }

            BaseStorageTaskPoint baseStorageTaskPoint = baseStorageTaskPointList.get(0);
            if ("5".equals(agvCallBackDTO.getMethod())) {
                baseStorageTaskPoint.setStorageTaskPointStatus((byte) 1);
            } else if ("6".equals(agvCallBackDTO.getMethod())) {
                baseStorageTaskPoint.setStorageTaskPointStatus((byte) 2);
            }
            baseStorageTaskPoint.setModifiedTime(new Date());
            baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPoint);

            TemVehicle temVehicle = new TemVehicle();
            if ("5".equals(agvCallBackDTO.getMethod())) {
                temVehicle = temVehicleFeignApi.detail(baseStorageTaskPointList.get(0).getVehicleId()).getData();
                if (!temVehicle.getVehicleCode().equals(agvCallBackDTO.getPodCode())) {
                    throw new BizErrorException("货架转移任务 : " + agvCallBackDTO.getTaskCode() + " 绑定的货架" + agvCallBackDTO.getPodCode()
                            + "与配送点 : " + baseStorageTaskPoint.getTaskPointCode() + "绑定的货架 ： " + temVehicle.getVehicleCode() + "不一致");
                }
            } else if ("6".equals(agvCallBackDTO.getMethod())) {
                SearchTemVehicle searchTemVehicle = new SearchTemVehicle();
                searchTemVehicle.setVehicleCode(agvCallBackDTO.getPodCode());
                List<TemVehicleDto> temVehicleDtoList = temVehicleFeignApi.findList(searchTemVehicle).getData();
                if (temVehicleDtoList.isEmpty()) {
                    throw new BizErrorException("货架转移任务 : " + agvCallBackDTO.getTaskCode() + " 没有找到对应的货架信息 : " + agvCallBackDTO.getPodCode());
                }
                BeanUtils.autoFillEqFields(temVehicleDtoList.get(0), temVehicle);
            }
            if ("5".equals(agvCallBackDTO.getMethod())) {
                temVehicle.setStorageTaskPointId(0l);
            } else if ("6".equals(agvCallBackDTO.getMethod())) {
                temVehicle.setStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
            }
            temVehicle.setRemark(agvCallBackDTO.getTaskCode());
            temVehicle.setModifiedUserId(user.getUserId());
            temVehicle.setModifiedTime(new Date());
            temVehicleFeignApi.update(temVehicle);

            // 更新AGV货架转移任务
            SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
            searchCallAgvAgvTask.setTaskCode(agvCallBackDTO.getTaskCode());
            List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
            CallAgvAgvTask callAgvAgvTask = new CallAgvAgvTask();
            if (!callAgvAgvTaskDtoList.isEmpty()) {
                BeanUtils.autoFillEqFields(callAgvAgvTaskDtoList.get(0), callAgvAgvTask);
                if ("5".equals(agvCallBackDTO.getMethod())) {
                    callAgvAgvTask.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
                } else if ("6".equals(agvCallBackDTO.getMethod())) {
                    callAgvAgvTask.setEndStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
                    callAgvAgvTask.setTaskStatus((byte) 3);
                }
                callAgvAgvTask.setModifiedUserId(user.getUserId());
                callAgvAgvTask.setModifiedTime(new Date());
                callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
            }
            if ("6".equals(agvCallBackDTO.getMethod())) {

                // 货架转移，通知SCM
                Example example = new Example(CallAgvVehicleReBarcode.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("vehicleId", temVehicle.getVehicleId()).andEqualTo("orgId", user.getOrganizationId());
                List<CallAgvVehicleReBarcode> callAgvVehicleReBarcodeList = callAgvVehicleReBarcodeMapper.selectByExample(example);
                if (!callAgvVehicleReBarcodeList.isEmpty()) {
                    Map<String, String> jsonMap = new HashMap<>();
                    jsonMap.put("inStrogeteCode", baseStorageTaskPoint.getStorageCode());
                    jsonMap.put("containerCode", temVehicle.getVehicleCode());
                    log.info("货架转移参数：" + JSONObject.toJSONString(jsonMap));
                    Map<String, Object> paramMap = callAgvVehicleReBarcodeService.scmParam(jsonMap, "mls.save.production.transfer");
                    log.info("货架转移，回传数据到SCM : " + JsonUtils.objectToJson(paramMap));
                    try {
                        String result = RestTemplateUtil.postForString(url, paramMap);
                        log.info("货架转移，回传数据到SCM结果 : " + JSONObject.parseObject(result));
                    } catch (Exception e) {

                    }
                }

                // 判断货架转移的终点是否有进行中的任务，存在就将该货架再次进行转移
                try {
                    SearchCallAgvAgvTask searchCallAgvAgvTask2 = new SearchCallAgvAgvTask();
                    searchCallAgvAgvTask2.setEndStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
                    searchCallAgvAgvTask2.setTaskStatus(2);
                    List<CallAgvAgvTaskDto> callAgvAgvTaskDtos = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask2));
                    if (StringUtils.isNotEmpty(callAgvAgvTaskDtos)) {
                        List<String> positionCodeList = new LinkedList<>();
                        positionCodeList.add(baseStorageTaskPoint.getXyzCode());

                        SearchBaseStorageTaskPoint searchBaseStorageTaskPoint2 = new SearchBaseStorageTaskPoint();
                        searchBaseStorageTaskPoint2.setWarehouseAreaId(baseStorageTaskPoint.getWarehouseAreaId());
                        searchBaseStorageTaskPoint2.setTaskPointType((byte) 2);
                        searchBaseStorageTaskPoint2.setStorageTaskPointStatus((byte) 1);
                        searchBaseStorageTaskPoint2.setIfOrderByUsePriority(1);
                        searchBaseStorageTaskPoint2.setNotHierarchicalCategory(baseStorageTaskPoint.getHierarchicalCategory());
                        List<BaseStorageTaskPoint> baseStorageTaskPoints = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint2).getData();
                        if (StringUtils.isEmpty(baseStorageTaskPoints)) {
                            callAgvAgvTask.setRemark("货架占用终点和任务：" + callAgvAgvTaskDtos.get(0).getTaskCode() + "，没有空闲配送点可供再次转移");
                            callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
                            return "0";
                        }
                        positionCodeList.add(baseStorageTaskPoints.get(0).getXyzCode());

                        String taskCode = callAgvVehicleReBarcodeService.genAgvSchedulingTask(temVehicle.getAgvTaskTemplate(), positionCodeList, temVehicle.getVehicleCode());

                        temVehicle.setStorageTaskPointId(0l);
                        temVehicle.setModifiedUserId(user.getUserId());
                        temVehicleFeignApi.update(temVehicle);

                        BaseStorageTaskPoint baseStorageTaskPointEnd = baseStorageTaskPoints.get(0);
                        baseStorageTaskPointEnd.setStorageTaskPointStatus((byte) 2);
                        baseStorageTaskPointEnd.setModifiedUserId(user.getUserId());
                        baseStorageTaskPointEnd.setModifiedTime(new Date());
                        baseStorageTaskPointEnd.setRemark("锁定配送目的点，等待货架：" + temVehicle.getVehicleCode() + "配送");
                        baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPointEnd);

                        log.info("==========启动agv执行货架 : " + temVehicle.getVehicleCode() + "再次进行转移作业任务==============\r\n");

                        temVehicle.setStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
                        redisUtil.set("2-" + taskCode, JSONObject.toJSONString(temVehicle));
                        log.info("=========记录当前货架 : " + temVehicle.getVehicleCode() + "再次进行转移作业任务载具对应的目的配送点 : key : " + "2-" + taskCode + " value : " + JSONObject.toJSONString(temVehicle) + "\r\n");

                        // 记录AGV任务
                        CallAgvAgvTask callAgvAgvTaskNew = new CallAgvAgvTask();
                        callAgvAgvTaskNew.setTaskCode(taskCode);
                        callAgvAgvTaskNew.setVehicleId(temVehicle.getVehicleId());
                        callAgvAgvTaskNew.setTaskStatus((byte) 2);
                        callAgvAgvTaskNew.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
                        callAgvAgvTaskNew.setEndStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
                        callAgvAgvTaskNew.setOperateType((byte) 4);
                        callAgvAgvTaskNew.setStatus((byte) 1);
                        callAgvAgvTaskNew.setOrgId(user.getOrganizationId());
                        callAgvAgvTaskNew.setCreateUserId(user.getUserId());
                        callAgvAgvTaskNew.setCreateTime(new Date());
                        callAgvAgvTaskNew.setIsDelete((byte) 1);
                        callAgvAgvTaskMapper.insertUseGeneratedKeys(callAgvAgvTaskNew);

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

                        // 货架转移，通知SCM
                        if (!callAgvAgvTaskBarcodeList.isEmpty()) {
                            callAgvAgvTaskBarcodeMapper.insertList(callAgvAgvTaskBarcodeList);
                            Map<String, String> jsonMap = new HashMap<>();
                            jsonMap.put("inStrogeteCode", baseStorageTaskPointEnd.getStorageCode());
                            jsonMap.put("containerCode", temVehicle.getVehicleCode());
                            log.info("货架再次转移参数：" + JSONObject.toJSONString(jsonMap));
                            Map<String, Object> map = callAgvVehicleReBarcodeService.scmParam(jsonMap, "mls.save.production.transfer");
                            redisUtil.set("scm-" + taskCode, JSONObject.toJSONString(map));
                            log.info("=========记录当前货架 : " + temVehicle.getVehicleCode() + "再次转移作业对应的出库单 : key : " + "scm-" + taskCode + " value : " + JSONObject.toJSONString(map) + "\r\n");
                        }
                    }
                } catch (Exception e) {

                }
            }
        }

        if (StringUtils.isNotEmpty(redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()))) {
            if ("2".equals(agvCallBackDTO.getMethod())) {
                TemVehicle temVehicle = BeanUtils.convertJson(redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()).toString(), new TypeToken<TemVehicle>(){}.getType());
                temVehicle.setModifiedUserId(user.getUserId());
                temVehicle.setModifiedTime(new Date());
                temVehicleFeignApi.update(temVehicle);

                BaseStorageTaskPoint baseStorageTaskPoint = baseFeignApi.baseStorageTaskPointDetail(temVehicle.getStorageTaskPointId()).getData();
                baseStorageTaskPoint.setRemark(temVehicle.getRemark());
                baseStorageTaskPoint.setModifiedTime(new Date());
                baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPoint);

                SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
                searchCallAgvAgvTask.setTaskCode(agvCallBackDTO.getTaskCode());
                List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
                if (!callAgvAgvTaskDtoList.isEmpty()) {
                    CallAgvAgvTask callAgvAgvTask = new CallAgvAgvTask();
                    callAgvAgvTask.setAgvTaskId(callAgvAgvTaskDtoList.get(0).getAgvTaskId());
                    callAgvAgvTask.setTaskStatus((byte) 3);
                    callAgvAgvTask.setModifiedTime(new Date());
                    if (StringUtils.isNotEmpty(callAgvAgvTask.getOption1())) {
                        callAgvAgvTask.setOption1("1");
                    }
                    callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
                }

                if (StringUtils.isNotEmpty(redisUtil.get("scm-" + agvCallBackDTO.getTaskCode()))) {
                    String jsonMap = redisUtil.get("scm-" + agvCallBackDTO.getTaskCode()).toString();
                    Map<String, Object> paramMap = JsonUtils.jsonToMap(jsonMap);
                    paramMap.put("timestamp", SCMSign.getTime());
                    String message = "";
                    if (paramMap.get("name").equals("mls.save.out.warehouse")) {
                        message = "货架 : " + temVehicle.getVehicleCode() + "AGV配送完成，叫料确认（生成出库单)";
                    } else if (paramMap.get("name").equals("mls.save.production.transfer")) {
                        message = "货架 : " + temVehicle.getVehicleCode() + "AGV配送完成，库位转移";
                    }
                    log.info(message + " : " + JsonUtils.objectToJson(paramMap));
                    try {
                        String result = RestTemplateUtil.postForString(url, paramMap);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        log.info(message + "结果 : " + jsonObject);
                        log.info("删除回传SCM任务redis记录的key : " + "scm-" + agvCallBackDTO.getTaskCode() + " value : " + jsonMap);
                        redisUtil.del("scm-" + agvCallBackDTO.getTaskCode());
                    } catch (Exception e) {

                    }
                }

                // 当前目的配送点是外层配送点且对应的内层配送点空闲，启动AGV任务将货架送至内层配送点
                if (StringUtils.isNotEmpty(baseStorageTaskPoint.getHierarchicalCategory())) {
                    SearchBaseStorageTaskPoint searchBaseStorageTaskPoint = new SearchBaseStorageTaskPoint();
                    searchBaseStorageTaskPoint.setHierarchicalCategory(baseStorageTaskPoint.getHierarchicalCategory());
                    searchBaseStorageTaskPoint.setStorageTaskPointStatus((byte) 1);
                    List<BaseStorageTaskPoint> baseStorageTaskPointList = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData();
                    if (!baseStorageTaskPointList.isEmpty() && !baseStorageTaskPointList.get(0).getStorageTaskPointId().equals(baseStorageTaskPoint.getStorageTaskPointId())
                    && baseStorageTaskPoint.getUsePriority() > baseStorageTaskPointList.get(0).getUsePriority()) {

                        BaseStorageTaskPoint baseStorageTaskPointEnd = baseStorageTaskPointList.get(0);
                        baseStorageTaskPointEnd.setStorageTaskPointStatus((byte) 2);
                        baseStorageTaskPointEnd.setModifiedUserId(user.getUserId());
                        baseStorageTaskPointEnd.setModifiedTime(new Date());
                        baseStorageTaskPointEnd.setRemark("锁定配送目的点，等待货架：" + temVehicle.getVehicleCode() + "配送");
                        baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPointEnd);

                        List<String> positionCodeList = new LinkedList<>();
                        positionCodeList.add(baseStorageTaskPoint.getXyzCode());
                        positionCodeList.add(baseStorageTaskPointList.get(0).getXyzCode());
                        String taskTyp = temVehicle.getAgvTaskTemplate();
                        String taskCode = callAgvVehicleReBarcodeService.genAgvSchedulingTask(taskTyp, positionCodeList, temVehicle.getVehicleCode());

                        temVehicle.setStorageTaskPointId(0l);
                        temVehicle.setModifiedUserId(user.getUserId());
                        temVehicleFeignApi.update(temVehicle);

                        log.info("==========启动agv执行货架 : " + temVehicle.getVehicleCode() + "进行库位内层转移作业任务==============\r\n");
                        baseStorageTaskPoint.setStorageTaskPointStatus((byte) 1);
                        baseStorageTaskPoint.setModifiedUserId(user.getUserId());
                        redisUtil.set("3-" + taskCode, JSONObject.toJSONString(baseStorageTaskPoint));
                        log.info("=========记录当前货架 : " + temVehicle.getVehicleCode() + "进行库位内层转移作业对应的起始配送点 : key : " + "3-" + taskCode + " value : " + JSONObject.toJSONString(baseStorageTaskPoint) + "\r\n");

                        temVehicle.setStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
                        redisUtil.set("2-" + taskCode, JSONObject.toJSONString(temVehicle));
                        log.info("=========记录当前货架 : " + temVehicle.getVehicleCode() + "进行库位内层转移作业载具对应的目的配送点 : key : " + "2-" + taskCode + " value : " + JSONObject.toJSONString(temVehicle) + "\r\n");

                        // 记录AGV任务
                        CallAgvAgvTask callAgvAgvTask = new CallAgvAgvTask();
                        callAgvAgvTask.setTaskCode(taskCode);
                        callAgvAgvTask.setVehicleId(temVehicle.getVehicleId());
                        callAgvAgvTask.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
                        callAgvAgvTask.setEndStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
                        callAgvAgvTask.setTaskStatus((byte) 2);
                        callAgvAgvTask.setOperateType((byte) 4);
                        callAgvAgvTask.setStatus((byte) 1);
                        callAgvAgvTask.setOrgId(user.getOrganizationId());
                        callAgvAgvTask.setCreateUserId(user.getUserId());
                        callAgvAgvTask.setCreateTime(new Date());
                        callAgvAgvTask.setIsDelete((byte) 1);
                        callAgvAgvTask.setRemark("货架外层转移到内层");
                        callAgvAgvTaskMapper.insertUseGeneratedKeys(callAgvAgvTask);
                        Example example = new Example(CallAgvVehicleReBarcode.class);
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo("vehicleId", temVehicle.getVehicleId()).andEqualTo("orgId", user.getOrganizationId());
                        List<CallAgvVehicleReBarcode> callAgvVehicleReBarcodeList = callAgvVehicleReBarcodeMapper.selectByExample(example);
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

                        // 外层库位转移到内层，通知SCM
                        if (!callAgvAgvTaskBarcodeList.isEmpty()) {
                            callAgvAgvTaskBarcodeMapper.insertList(callAgvAgvTaskBarcodeList);
                            Map<String, String> jsonMap = new HashMap<>();
                            jsonMap.put("inStrogeteCode", baseStorageTaskPointEnd.getStorageCode());
                            jsonMap.put("containerCode", temVehicle.getVehicleCode());
                            log.info("外层库位转移到内层参数：" + JSONObject.toJSONString(jsonMap));
                            Map<String, Object> map = callAgvVehicleReBarcodeService.scmParam(jsonMap, "mls.save.production.transfer");
                            redisUtil.set("scm-" + taskCode, JSONObject.toJSONString(map));
                            log.info("=========记录当前货架 : " + temVehicle.getVehicleCode() + "进行库位内层转移作业对应的出库单 : key : " + "scm-" + taskCode + " value : " + JSONObject.toJSONString(map) + "\r\n");
                        }
                    }
                }
            } else if ("3".equals(agvCallBackDTO.getMethod())) {
                BaseStorageTaskPoint baseStorageTaskPoint = BeanUtils.convertJson(redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()).toString(), new TypeToken<BaseStorageTaskPoint>(){}.getType());
                baseStorageTaskPoint.setModifiedTime(new Date());
                baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPoint);
            } else if ("4".equals(agvCallBackDTO.getMethod())) {
                schedlingAgvTask(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode());
            }
            log.info("收到RCS系统回传AGV任务状态，释放对应的redis : " + agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode() + " value : " + redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()) + "\r\n");
            redisUtil.del(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode());
        }

        return "0";
    }

    @Override
    @Transactional
    @LcnTransaction
    public int warnCallback(WarnCallbackDTO warnCallbackDTO) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<WarnCallbackData> dataList = warnCallbackDTO.getData();
        List<String> taskCodes = new ArrayList<>();
        for (WarnCallbackData data : dataList) {
            if (taskCodes.contains(data.getTaskCode())) {
                continue;
            }
            taskCodes.add(data.getTaskCode());
            if ("无有效路径(路径上存在货架或障碍区阻挡)，被货架阻挡".contains(data.getWarnContent())) {
                Example example = new Example(CallAgvAgvTask.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("taskCode", data.getTaskCode());
                CallAgvAgvTask callAgvAgvTask = callAgvAgvTaskMapper.selectOneByExample(example);

                BaseStorageTaskPoint baseStorageTaskPoint = null;
                if (StringUtils.isNotEmpty(redisUtil.get("3-" + data.getTaskCode()))) {
                    baseStorageTaskPoint = baseFeignApi.baseStorageTaskPointDetail(callAgvAgvTask.getStartStorageTaskPointId()).getData();
                } else if (StringUtils.isNotEmpty(redisUtil.get("2-" + data.getTaskCode()))) {
                    baseStorageTaskPoint = baseFeignApi.baseStorageTaskPointDetail(callAgvAgvTask.getEndStorageTaskPointId()).getData();
                }
                if (StringUtils.isNotEmpty(baseStorageTaskPoint) && StringUtils.isNotEmpty(baseStorageTaskPoint.getHierarchicalCategory())) {
                    List<String> positionCodeList = new LinkedList<>();
                    SearchBaseStorageTaskPoint searchBaseStorageTaskPoint = new SearchBaseStorageTaskPoint();
                    searchBaseStorageTaskPoint.setHierarchicalCategory(baseStorageTaskPoint.getHierarchicalCategory());
                    searchBaseStorageTaskPoint.setIfOrderByUsePriority(1);
                    List<BaseStorageTaskPoint> baseStorageTaskPointList = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint).getData();
                    if (baseStorageTaskPointList.size() > 1 && !baseStorageTaskPointList.get(1).getStorageTaskPointId().equals(baseStorageTaskPoint.getStorageTaskPointId())) {
                        BaseStorageTaskPoint baseStorageTaskPointStart = baseStorageTaskPointList.get(1);
                        if (StringUtils.isNotEmpty(baseStorageTaskPointStart.getVehicleId())) {
                            callAgvAgvTask.setOption1("挡路配送点 + " + baseStorageTaskPointStart.getTaskPointName() + "没有找到绑定的货架");
                            callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
                            return 0;
                        }
                        TemVehicle temVehicle = temVehicleFeignApi.detail(baseStorageTaskPointStart.getVehicleId()).getData();
                        SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
                        searchCallAgvAgvTask.setVehicleId(temVehicle.getVehicleId());
                        searchCallAgvAgvTask.setTaskStatusList(Arrays.asList(2));
                        List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
                        if (!callAgvAgvTaskDtoList.isEmpty()) {
                            throw new BizErrorException("当前货架存在未完成任务，无法进行货架转移");
                        }

                        positionCodeList.add(baseStorageTaskPointStart.getXyzCode());
                        SearchBaseStorageTaskPoint searchBaseStorageTaskPoint2 = new SearchBaseStorageTaskPoint();
                        searchBaseStorageTaskPoint2.setWarehouseAreaId(baseStorageTaskPointStart.getWarehouseAreaId());
                        searchBaseStorageTaskPoint2.setTaskPointType((byte) 2);
                        searchBaseStorageTaskPoint2.setStorageTaskPointStatus((byte) 1);
                        searchBaseStorageTaskPoint2.setIfOrderByUsePriority(1);
                        List<BaseStorageTaskPoint> baseStorageTaskPoints = baseFeignApi.findBaseStorageTaskPointList(searchBaseStorageTaskPoint2).getData();
                        if (StringUtils.isEmpty(baseStorageTaskPoints)) {
                            callAgvAgvTask.setOption1("没有空闲的库位将挡路货架转移");
                            callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
                            return 0;
                        }
                        positionCodeList.add(baseStorageTaskPoints.get(0).getXyzCode());

                        // 启动AGV进行货架转移
                        log.info("==========启动AGV进行货架 : " + temVehicle.getVehicleCode() + "转移任务==============\r\n");
                        String taskCode = callAgvVehicleReBarcodeService.genAgvSchedulingTask(temVehicle.getAgvTaskTemplate(), positionCodeList, temVehicle.getVehicleCode());

                        temVehicle.setStorageTaskPointId(0l);
                        temVehicle.setModifiedUserId(user.getUserId());
                        temVehicle.setModifiedTime(new Date());
                        temVehicle.setRemark(taskCode);
                        temVehicleFeignApi.update(temVehicle);

                        BaseStorageTaskPoint baseStorageTaskPointEnd = baseStorageTaskPoints.get(0);
                        baseStorageTaskPointEnd.setStorageTaskPointStatus((byte) 2);
                        baseStorageTaskPointEnd.setModifiedUserId(user.getUserId());
                        baseStorageTaskPointEnd.setModifiedTime(new Date());
                        baseStorageTaskPointEnd.setRemark("锁定配送目的点，等待货架：" + temVehicle.getVehicleCode() + "配送");
                        baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPointEnd);

                        baseStorageTaskPointStart.setStorageTaskPointStatus((byte) 1);
                        baseStorageTaskPointStart.setModifiedUserId(user.getUserId());
                        redisUtil.set("3-" + taskCode, JSONObject.toJSONString(baseStorageTaskPointStart));
                        log.info("=========记录当前货架 : " + temVehicle.getVehicleCode() + "转移任务作业对应的起始配送点 : key : " + "3-" + taskCode + " value : " + JSONObject.toJSONString(baseStorageTaskPointStart) + "\r\n");

                        temVehicle.setStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
                        redisUtil.set("2-" + taskCode, JSONObject.toJSONString(temVehicle));
                        log.info("=========记录当前货架 : " + temVehicle.getVehicleCode() + "转移任务作业对应的目的配送点 : key : " + "2-" + taskCode + " value : " + JSONObject.toJSONString(temVehicle) + "\r\n");

                        // 记录AGV任务
                        CallAgvAgvTask callAgvAgvTaskNew = new CallAgvAgvTask();
                        callAgvAgvTaskNew.setTaskCode(taskCode);
                        callAgvAgvTaskNew.setVehicleId(temVehicle.getVehicleId());
                        callAgvAgvTaskNew.setTaskStatus((byte) 2);
                        callAgvAgvTaskNew.setStartStorageTaskPointId(baseStorageTaskPointStart.getStorageTaskPointId());
                        callAgvAgvTaskNew.setEndStorageTaskPointId(baseStorageTaskPointEnd.getStorageTaskPointId());
                        callAgvAgvTaskNew.setOperateType((byte) 4);
                        callAgvAgvTaskNew.setStatus((byte) 1);
                        callAgvAgvTaskNew.setOrgId(user.getOrganizationId());
                        callAgvAgvTaskNew.setCreateUserId(user.getUserId());
                        callAgvAgvTaskNew.setCreateTime(new Date());
                        callAgvAgvTaskNew.setIsDelete((byte) 1);
                        callAgvAgvTaskMapper.insertUseGeneratedKeys(callAgvAgvTaskNew);

                        Example example2 = new Example(CallAgvVehicleReBarcode.class);
                        Example.Criteria criteria2 = example2.createCriteria();
                        criteria2.andEqualTo("vehicleId", temVehicle.getVehicleId()).andEqualTo("orgId", user.getOrganizationId());
                        List<CallAgvVehicleReBarcode> callAgvVehicleReBarcodeList = callAgvVehicleReBarcodeMapper.selectByExample(example2);

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

                        // 货架转移，通知SCM
                        if (!callAgvAgvTaskBarcodeList.isEmpty()) {
                            callAgvAgvTaskBarcodeMapper.insertList(callAgvAgvTaskBarcodeList);
                            Map<String, String> jsonMap = new HashMap<>();
                            jsonMap.put("inStrogeteCode", baseStorageTaskPointEnd.getStorageCode());
                            jsonMap.put("containerCode", temVehicle.getVehicleCode());
                            log.info("货架转移参数：" + JSONObject.toJSONString(jsonMap));
                            Map<String, Object> map = callAgvVehicleReBarcodeService.scmParam(jsonMap, "mls.save.production.transfer");
                            redisUtil.set("scm-" + taskCode, JSONObject.toJSONString(map));
                            log.info("=========记录当前货架 : " + temVehicle.getVehicleCode() + "进行库位内层转移作业对应的出库单 : key : " + "scm-" + taskCode + " value : " + JSONObject.toJSONString(map) + "\r\n");
                        }
                    }
                }
            }
        }

        return 1;
    }

    private void schedlingAgvTask(String key) throws Exception {
        String type = redisUtil.get(key).toString();
        List<CallAgvDistributionDto> callAgvDistributionDtoList = BeanUtils.convertJson(redisUtil.get(type).toString(), new TypeToken<List<CallAgvDistributionDto>>() {}.getType());
        log.info("=========获取等待的电梯作业任务队列 : key : " + type + " value : " + JSONObject.toJSONString(callAgvDistributionDtoList) + "\r\n");

        if (callAgvDistributionDtoList.size() > 1) {
            CallAgvDistributionDto callAgvDistributionDto = callAgvDistributionDtoList.get(1);
            try {
                String taskCode = callAgvVehicleReBarcodeService.callAgvDistribution(callAgvDistributionDto.getVehicleId(), callAgvDistributionDto.getWarehouseAreaId(),callAgvDistributionDto.getStorageTaskPointId(), callAgvDistributionDto.getType(), false);
                callAgvDistributionDtoList.remove(0);
                redisUtil.set(type, JSONObject.toJSONString(callAgvDistributionDtoList));
                log.info("==========记录下一个等待的AGV电梯任务队列 : key " + type  + " , value : " + JSONObject.toJSONString(callAgvDistributionDtoList));
            } catch (Exception e) {
                log.info("==========电梯等待任务启动失败原因：" + e.getMessage());
                e.printStackTrace();
                CallAgvAgvTask callAgvAgvTask = callAgvAgvTaskMapper.selectByPrimaryKey(callAgvDistributionDto.getAgvTaskId());
                callAgvAgvTask.setTaskStatus((byte) 4);
                callAgvAgvTask.setRemark(e.getMessage());
                callAgvAgvTask.setModifiedTime(new Date());
                callAgvAgvTaskService.update(callAgvAgvTask);
                callAgvDistributionDtoList.remove(0);
                redisUtil.set(type, JSONObject.toJSONString(callAgvDistributionDtoList));
                log.info("==========电梯等待任务启动失败，更新等待的AGV电梯任务队列 : key " + type  + " , value : " + JSONObject.toJSONString(callAgvDistributionDto) + "\r\n");

                // 继续执行下面的电梯等待任务
                if (callAgvDistributionDtoList.size() > 1) {
                    log.info("==========电梯等待任务启动失败，继续执行下个电梯排队任务");
                    schedlingAgvTask(key);
                } else {
                    log.info("没有下个电梯等待任务，释放redis记录 key : " + type + " value : " + redisUtil.get(type) + "\r\n");
                    redisUtil.del(type);
                }
            } finally {
                log.info("删除货架 : " + callAgvDistributionDto.getVehicleCode() + "对应的电梯等待任务 : " + callAgvDistributionDto.getAgvTaskId());
                redisUtil.del(callAgvDistributionDto.getVehicleCode());
            }
        } else {
            log.info("没有下个电梯等待任务，释放redis记录 key : " + type + " value : " + redisUtil.get(type) + "\r\n");
            redisUtil.del(type);
        }
    }
}
