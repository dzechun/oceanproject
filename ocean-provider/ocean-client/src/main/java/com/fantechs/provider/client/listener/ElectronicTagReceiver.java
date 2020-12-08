package com.fantechs.provider.client.listener;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.server.impl.FanoutSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lfz on 2020/11/27.
 */
@Component
public class ElectronicTagReceiver {
    @Autowired
    private FanoutSender fanoutSender;

    // 监听标签队列
    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String  jsonObject ){
        MQResponseEntity  mqResponseEntity=JsonUtils.jsonToPojo(jsonObject,MQResponseEntity.class);
        //电子标签熄灭动作
        if(mqResponseEntity.getCode()==106){
           if(StringUtils.isEmpty(mqResponseEntity)){
               mqResponseEntity.setCode(999);
           }else {

           }
        }else if(mqResponseEntity.getCode()==107){

        }else if(mqResponseEntity.getCode()==110){

        }else if(mqResponseEntity.getCode()==112){

        }else if(mqResponseEntity.getCode()==113){

        }
        fanoutSender.send(RabbitConfig.FANOUT_QUEUE1, JSONObject.toJSONString(mqResponseEntity));
    }
}
