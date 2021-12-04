package com.fantechs.provider.guest.callagv.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.agv.dto.AgvCallBackDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvAgvTaskDto;
import com.fantechs.common.base.general.dto.callagv.GenAgvSchedulingTaskDTO;
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
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.guest.callagv.mapper.CallAgvAgvTaskBarcodeMapper;
import com.fantechs.provider.guest.callagv.mapper.CallAgvAgvTaskMapper;
import com.fantechs.provider.guest.callagv.mapper.CallAgvVehicleReBarcodeMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleReBarcodeService;
import com.fantechs.provider.guest.callagv.service.RcsCallBackService;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    private CallAgvAgvTaskMapper callAgvAgvTaskMapper;

    @Resource
    private CallAgvVehicleReBarcodeMapper callAgvVehicleReBarcodeMapper;

    @Resource
    private CallAgvAgvTaskBarcodeMapper callAgvAgvTaskBarcodeMapper;

    @Override
    @Transactional
    @LcnTransaction
    public String agvCallback(AgvCallBackDTO agvCallBackDTO) throws Exception {

        if (StringUtils.isEmpty(
                agvCallBackDTO.getTaskCode(),
                agvCallBackDTO.getRobotCode())) {

            throw new Exception("通知MES失败，没有找到任务单号或AGV编号！");
        }
        if (agvCallBackDTO.getRobotCode().equals("-1")) {
            throw new BizErrorException("没有找到对应的AGV编号：" + agvCallBackDTO.getRobotCode());
        }

        if (Integer.valueOf(agvCallBackDTO.getMethod()) > 4) {
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
            temVehicle.setModifiedTime(new Date());
            temVehicleFeignApi.update(temVehicle);

            // 记录AGV货架转移任务
            CallAgvAgvTask callAgvAgvTask = new CallAgvAgvTask();
            callAgvAgvTask.setTaskCode(agvCallBackDTO.getTaskCode());
            callAgvAgvTask.setVehicleId(temVehicle.getVehicleId());
            if ("5".equals(agvCallBackDTO.getMethod())) {
                callAgvAgvTask.setStartStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
            } else if ("6".equals(agvCallBackDTO.getMethod())) {
                callAgvAgvTask.setEndStorageTaskPointId(baseStorageTaskPoint.getStorageTaskPointId());
            }
            callAgvAgvTask.setOperateType((byte) 4);
            callAgvAgvTask.setStatus((byte) 1);
            callAgvAgvTask.setOrgId(baseStorageTaskPoint.getOrgId());
            callAgvAgvTask.setIsDelete((byte) 1);
            if ("5".equals(agvCallBackDTO.getMethod())) {
                callAgvAgvTask.setTaskStatus((byte) 2);
                callAgvAgvTask.setCreateTime(new Date());
                callAgvAgvTaskMapper.insertUseGeneratedKeys(callAgvAgvTask);

                Example example = new Example(CallAgvVehicleReBarcode.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("vehicleId", temVehicle.getVehicleId()).andEqualTo("orgId", temVehicle.getOrgId());
                List<CallAgvVehicleReBarcode> callAgvVehicleReBarcodeList = callAgvVehicleReBarcodeMapper.selectByExample(example);

                if (!callAgvVehicleReBarcodeList.isEmpty()) {
                    List<CallAgvAgvTaskBarcode> callAgvAgvTaskBarcodeList = new LinkedList<>();
                    for (CallAgvVehicleReBarcode callAgvVehicleReBarcode : callAgvVehicleReBarcodeList) {
                        CallAgvAgvTaskBarcode callAgvAgvTaskBarcode = new CallAgvAgvTaskBarcode();
                        callAgvAgvTaskBarcode.setAgvTaskId(callAgvAgvTask.getAgvTaskId());
                        callAgvAgvTaskBarcode.setBarcodeId(callAgvVehicleReBarcode.getBarcodeId());
                        callAgvAgvTaskBarcode.setStatus((byte) 1);
                        callAgvAgvTaskBarcode.setOrgId(baseStorageTaskPoint.getOrgId());
                        callAgvAgvTaskBarcode.setCreateTime(new Date());
                        callAgvAgvTaskBarcodeList.add(callAgvAgvTaskBarcode);
                    }
                    callAgvAgvTaskBarcodeMapper.insertList(callAgvAgvTaskBarcodeList);
                }
            } else if ("6".equals(agvCallBackDTO.getMethod())) {
                SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
                searchCallAgvAgvTask.setTaskCode(agvCallBackDTO.getTaskCode());
                List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
                if (!callAgvAgvTaskDtoList.isEmpty()) {
                    callAgvAgvTask.setAgvTaskId(callAgvAgvTaskDtoList.get(0).getAgvTaskId());
                    callAgvAgvTask.setTaskStatus((byte) 3);
                    callAgvAgvTask.setModifiedTime(new Date());
                    callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
                }
            }
        }

        if (StringUtils.isNotEmpty(redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()))) {
            if ("2".equals(agvCallBackDTO.getMethod())) {
                TemVehicle temVehicle = BeanUtils.convertJson(redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()).toString(), new TypeToken<TemVehicle>(){}.getType());
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
                    callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
                }
            } else if ("3".equals(agvCallBackDTO.getMethod())) {
                BaseStorageTaskPoint baseStorageTaskPoint = BeanUtils.convertJson(redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()).toString(), new TypeToken<BaseStorageTaskPoint>(){}.getType());
                baseStorageTaskPoint.setModifiedTime(new Date());
                baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPoint);
            } else if ("4".equals(agvCallBackDTO.getMethod())) {
                String type = redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()).toString();
                List<GenAgvSchedulingTaskDTO> genAgvSchedulingTaskDTOList = BeanUtils.convertJson(redisUtil.get(type).toString(), new TypeToken<List<GenAgvSchedulingTaskDTO>>() {}.getType());
                log.info("=========获取等待的电梯作业任务队列 : key : " + type + " value : " + JSONObject.toJSONString(genAgvSchedulingTaskDTOList) + "\r\n");

                if (genAgvSchedulingTaskDTOList.size() > 1) {
                    String taskCode = callAgvVehicleReBarcodeService.genAgvSchedulingTask(genAgvSchedulingTaskDTOList.get(1).getTaskTyp(), genAgvSchedulingTaskDTOList.get(1).getPositionCodeList(), genAgvSchedulingTaskDTOList.get(1).getPodCode());
                    log.info("==========启动agv执行下一个等待的电梯作业任务 : " + taskCode + "==============\r\n");

                    SearchCallAgvAgvTask searchCallAgvAgvTask = new SearchCallAgvAgvTask();
                    searchCallAgvAgvTask.setTaskCode(genAgvSchedulingTaskDTOList.get(1).getCallAgvAgvTask().getTaskCode());
                    List<CallAgvAgvTaskDto> callAgvAgvTaskDtoList = callAgvAgvTaskMapper.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
                    if (!callAgvAgvTaskDtoList.isEmpty()) {
                        CallAgvAgvTask callAgvAgvTask = genAgvSchedulingTaskDTOList.get(1).getCallAgvAgvTask();
                        callAgvAgvTask.setAgvTaskId(callAgvAgvTaskDtoList.get(0).getAgvTaskId());
                        callAgvAgvTask.setRemark(callAgvAgvTask.getTaskCode());
                        callAgvAgvTask.setTaskCode(taskCode);
                        callAgvAgvTask.setTaskStatus((byte) 2);
                        callAgvAgvTask.setCreateTime(new Date());
                        callAgvAgvTask.setModifiedTime(new Date());
                        callAgvAgvTaskMapper.updateByPrimaryKeySelective(callAgvAgvTask);
                    }

                    BaseStorageTaskPoint baseStorageTaskPoint = genAgvSchedulingTaskDTOList.get(1).getBaseStorageTaskPoint();
                    redisUtil.set("3-" + taskCode, JSONObject.toJSONString(baseStorageTaskPoint));
                    log.info("=========记录下一个等待的电梯作业任务对应的起始配送点 : key : " + "3-" + taskCode + " value : " + JSONObject.toJSONString(baseStorageTaskPoint) + "\r\n");

                    TemVehicle temVehicle = genAgvSchedulingTaskDTOList.get(1).getTemVehicle();
                    temVehicle.setRemark(taskCode);
                    redisUtil.set("2-" + taskCode, JSONObject.toJSONString(temVehicle));
                    log.info("=========记录下一个等待的电梯作业任务载具对应的目的配送点 : key : " + "2-" + taskCode + " value : " + JSONObject.toJSONString(temVehicle) + "\r\n");

                    genAgvSchedulingTaskDTOList.remove(0);
                    redisUtil.set("4-" + taskCode, type);
                    log.info("=========记录下一个等待的AGV电梯任务作业对应的配送方式 : key : " + "4-" + taskCode + " value : " + type + "\r\n");

                    redisUtil.set(type, JSONObject.toJSONString(genAgvSchedulingTaskDTOList));
                    log.info("==========记录下一个等待的AGV电梯任务队列 : key " + type  + " , value : " + JSONObject.toJSONString(genAgvSchedulingTaskDTOList));
                } else {
                    log.info("没有下个电梯等待任务，释放redis记录 key : " + type + " value : " + redisUtil.get(type) + "\r\n");
                    redisUtil.del(type);
                }
            }
            log.info("收到RCS系统回传AGV任务状态，释放对应的redis : " + agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode() + " value : " + redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()) + "\r\n");
            redisUtil.del(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode());
        }

        return "0";
    }
}
