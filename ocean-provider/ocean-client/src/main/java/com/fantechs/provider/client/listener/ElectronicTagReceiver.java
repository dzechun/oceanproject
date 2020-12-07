package com.fantechs.provider.client.listener;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.server.impl.FanoutSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by lfz on 2020/11/27.
 */
@Component
public class ElectronicTagReceiver {
    @Autowired
    private FanoutSender fanoutSender;

    // queues是指要监听的队列的名字
    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String  jsonObject ) throws UnsupportedEncodingException {
        MQResponseEntity  mqResponseEntity=JsonUtils.jsonToPojo(jsonObject,MQResponseEntity.class);
        //电子标签熄灭动作
        if(mqResponseEntity.getCode()==106){
           Map<String, Object> json = JsonUtils.jsonToMap(mqResponseEntity.getData().toString());
           if(StringUtils.isEmpty(json)){
               mqResponseEntity.setCode(999);
               fanoutSender.send("testtopic",mqResponseEntity);
                return;
           }
        }else if(mqResponseEntity.getCode()==107){

        }else if(mqResponseEntity.getCode()==110){

        }else if(mqResponseEntity.getCode()==112){

        }else if(mqResponseEntity.getCode()==113){

        }
    }
}
