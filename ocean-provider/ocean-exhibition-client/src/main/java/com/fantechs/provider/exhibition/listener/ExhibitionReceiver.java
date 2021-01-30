package com.fantechs.provider.exhibition.listener;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.agv.dto.MaterialAndPositionCodeEnum;
import com.fantechs.common.base.agv.dto.PositionCodePath;
import com.fantechs.common.base.agv.dto.RcsResponseDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.SmtProcessListProcessDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.agv.AgvFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.exhibition.config.RabbitConfig;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class ExhibitionReceiver {

    private static final Logger log = LoggerFactory.getLogger(ExhibitionReceiver.class);

    @Resource
    private AgvFeignApi agvFeignApi;

    @Resource
    private PMFeignApi  pmFeignApi;

    // 监听客户端队列
    @RabbitListener(queues = RabbitConfig.TOPIC_LISTENER_WORK_QUEUE)
    public void receiveTopicWorkQueue(byte[]  bytes ) throws Exception {
        String encoded= new String(bytes,"UTF-8");
        MQResponseEntity mqResponseEntity= JsonUtils.jsonToPojo(encoded,MQResponseEntity.class);
        log.info("监听客户端队列 : TOPIC_LISTENER_WORK_QUEUE 信息 : " + JSONObject.toJSONString(mqResponseEntity));
        // 控制伸缩臂取料完成
        if (mqResponseEntity.getCode() == 1011) {
            // agv继续执行任务
            /*Map<String, Object> map = new HashMap<>();
            map.put("agvCode", "2249");
            String result = agvFeignApi.continueTask(map).getData();*/
            // 最后一道工序完工
        } else if (mqResponseEntity.getCode() == 1012) {
            // 驱动AGV去配送成品
            String startPositionCode = "";
            String endPositionCode = "";
            for(MaterialAndPositionCodeEnum.MaterialAndPositionCode materialAndPositionCode : MaterialAndPositionCodeEnum.MaterialAndPositionCode.values()){
                if("911".equals(materialAndPositionCode.getMaterialCode())) {
                    startPositionCode = materialAndPositionCode.getStartPositionCode();
                    endPositionCode = materialAndPositionCode.getEndPositionCode();
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("taskTyp", "ZT04");
            List<PositionCodePath> positionCodePathList = new LinkedList<>();
            // 起始地标条码
            PositionCodePath positionCodePath = new PositionCodePath();
            positionCodePath.setPositionCode(startPositionCode);
            positionCodePath.setType("00");
            positionCodePathList.add(positionCodePath);
            // 目标位置条码
            PositionCodePath positionCodePath2 = new PositionCodePath();
            positionCodePath2.setPositionCode(endPositionCode);
            positionCodePath2.setType("00");
            positionCodePathList.add(positionCodePath2);
            map.put("positionCodePath", positionCodePathList);
            String result = agvFeignApi.genAgvSchedulingTask(map).getData();
            RcsResponseDTO rcsResponseDTO = BeanUtils.convertJson(result, new TypeToken<RcsResponseDTO>(){}.getType());
            log.info("启动AGV配送任务：请求参数：" + JSONObject.toJSONString(map) + "， 返回结果：" + JSONObject.toJSONString(rcsResponseDTO));
        }
    }


    // 监听客户端队列
    @RabbitListener(queues = RabbitConfig.TOPIC_PROCESS_WORK_QUEUE)
    public void receiveTopicProcessWorkQueue(byte[]  bytes ) throws Exception {
        String encoded= new String(bytes,"UTF-8");
        MQResponseEntity mqResponseEntity= JsonUtils.jsonToPojo(encoded,MQResponseEntity.class);
        log.info("监听客户端队列 : TOPIC_LISTENER_WORK_QUEUE 信息 : " + JSONObject.toJSONString(mqResponseEntity));
        if (mqResponseEntity.getCode() == 1101) {
            SmtProcessListProcess processListProcess =  JsonUtils.jsonToPojo(mqResponseEntity.getData().toString(),SmtProcessListProcess.class);
            SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
            searchSmtProcessListProcess.setWorkOrderCardPoolId(processListProcess.getWorkOrderCardPoolId());
            List<SmtProcessListProcessDto>  smtProcessListProcessDtoList=pmFeignApi.findSmtProcessListProcessList( searchSmtProcessListProcess).getData();
            if(StringUtils.isEmpty(smtProcessListProcessDtoList)){
                throw new BizErrorException("未找到对应产品条码流转卡");
            }
            SmtProcessListProcessDto firstProcessListProcessDto = smtProcessListProcessDtoList.get(0);
            SmtProcessListProcessDto lastProcessListProcessDto = smtProcessListProcessDtoList.get(smtProcessListProcessDtoList.size()-1);
            //首工序
            if(processListProcess.getProcessId() == firstProcessListProcessDto.getProcessId()){

            }else if(processListProcess.getProcessId() == lastProcessListProcessDto.getProcessId()){ //最后一道工序

            }

        } else if (mqResponseEntity.getCode() == 1011) {
            // agv继续执行任务
            Map<String, Object> map = new HashMap<>();
            map.put("agvCode", "2249");
            String result = agvFeignApi.continueTask(map).getData();
            // 最后一道工序完工
        } else if (mqResponseEntity.getCode() == 1012) {
            // 驱动AGV去配送成品
            String startPositionCode = "";
            String endPositionCode = "";
            for(MaterialAndPositionCodeEnum.MaterialAndPositionCode materialAndPositionCode : MaterialAndPositionCodeEnum.MaterialAndPositionCode.values()){
                if("911".equals(materialAndPositionCode.getMaterialCode())) {
                    startPositionCode = materialAndPositionCode.getStartPositionCode();
                    endPositionCode = materialAndPositionCode.getEndPositionCode();
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("taskTyp", "ZT04");
            List<PositionCodePath> positionCodePathList = new LinkedList<>();
            // 起始地标条码
            PositionCodePath positionCodePath = new PositionCodePath();
            positionCodePath.setPositionCode(startPositionCode);
            positionCodePath.setType("00");
            positionCodePathList.add(positionCodePath);
            // 目标位置条码
            PositionCodePath positionCodePath2 = new PositionCodePath();
            positionCodePath2.setPositionCode(endPositionCode);
            positionCodePath2.setType("00");
            positionCodePathList.add(positionCodePath2);
            map.put("positionCodePath", positionCodePathList);
            String result = agvFeignApi.genAgvSchedulingTask(map).getData();
            RcsResponseDTO rcsResponseDTO = BeanUtils.convertJson(result, new TypeToken<RcsResponseDTO>(){}.getType());
            log.info("启动AGV配送任务：请求参数：" + JSONObject.toJSONString(map) + "， 返回结果：" + JSONObject.toJSONString(rcsResponseDTO));
        }
    }

}
