package com.fantechs.provider.guest.callagv.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.agv.dto.AgvCallBackDTO;
import com.fantechs.common.base.general.dto.callagv.CallAgvAgvTaskDto;
import com.fantechs.common.base.general.dto.callagv.GenAgvSchedulingTaskDTO;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvAgvTask;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.guest.callagv.mapper.CallAgvAgvTaskMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleReBarcodeService;
import com.fantechs.provider.guest.callagv.service.RcsCallBackService;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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

    @Override
    public String agvCallback(AgvCallBackDTO agvCallBackDTO) throws Exception {

        if (StringUtils.isEmpty(
                agvCallBackDTO.getTaskCode(),
                agvCallBackDTO.getRobotCode())) {

            throw new Exception("通知MES失败，没有找到任务单号或AGV编号！");
        }
        if (agvCallBackDTO.getRobotCode().equals("-1")) {
            throw new Exception("没有找到对应的AGV编号：" + agvCallBackDTO.getRobotCode());
        }

        if (StringUtils.isNotEmpty(redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()))) {
            if ("2".equals(agvCallBackDTO.getMethod())) {
                TemVehicle temVehicle = BeanUtils.convertJson(redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()).toString(), new TypeToken<TemVehicle>(){}.getType());
                temVehicle.setModifiedTime(new Date());
                temVehicleFeignApi.update(temVehicle);

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
