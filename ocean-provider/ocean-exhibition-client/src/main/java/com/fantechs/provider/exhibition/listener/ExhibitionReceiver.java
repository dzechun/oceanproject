package com.fantechs.provider.exhibition.listener;

import com.fantechs.common.base.agv.dto.MaterialAndPositionCodeEnum;
import com.fantechs.common.base.agv.dto.PositionCodePath;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.provider.api.agv.AgvFeignApi;
import com.fantechs.provider.exhibition.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class ExhibitionReceiver {

    @Autowired
    private AgvFeignApi agvFeignApi;

    // 监听客户端队列
    @RabbitListener(queues = RabbitConfig.TOPIC_WORK_QUEUE)
    public void receiveTopicWorkQueue(byte[]  bytes ) throws Exception {
        String encoded= new String(bytes,"UTF-8");
        MQResponseEntity mqResponseEntity= JsonUtils.jsonToPojo(encoded,MQResponseEntity.class);
        // 控制伸缩臂取料完成
        if (mqResponseEntity.getCode() == 1011) {
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
            map.put("taskTyp", "c02");
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
            String taskCode = agvFeignApi.genAgvSchedulingTask(map).getData();
        }
    }

}
