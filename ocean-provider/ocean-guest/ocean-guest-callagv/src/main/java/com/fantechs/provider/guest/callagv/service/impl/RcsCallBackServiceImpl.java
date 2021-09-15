package com.fantechs.provider.guest.callagv.service.impl;

import com.fantechs.common.base.agv.dto.AgvCallBackDTO;
import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.tem.TemVehicleFeignApi;
import com.fantechs.provider.guest.callagv.service.RcsCallBackService;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class RcsCallBackServiceImpl implements RcsCallBackService {

    @Resource
    private TemVehicleFeignApi temVehicleFeignApi;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    RedisUtil redisUtil;

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
            } else if ("3".equals(agvCallBackDTO.getMethod())) {
                BaseStorageTaskPoint baseStorageTaskPoint = BeanUtils.convertJson(redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()).toString(), new TypeToken<BaseStorageTaskPoint>(){}.getType());
                baseStorageTaskPoint.setModifiedTime(new Date());
                baseFeignApi.updateBaseStorageTaskPoint(baseStorageTaskPoint);
            }
            log.info("收到RCS系统回传AGV任务状态，释放对应的redis : " + agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode() + " value : " + redisUtil.get(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode()) + "\r\n");
            redisUtil.del(agvCallBackDTO.getMethod() + "-" + agvCallBackDTO.getTaskCode());
        }

        return "0";
    }
}
