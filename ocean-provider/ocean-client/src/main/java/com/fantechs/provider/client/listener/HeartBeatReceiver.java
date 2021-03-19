package com.fantechs.provider.client.listener;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Component
public class HeartBeatReceiver {

    private static final Logger log = LoggerFactory.getLogger(HeartBeatReceiver.class);

    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;

    // 监听设备心跳队列
    @RabbitListener(queues = RabbitConfig.TOPIC_HEARTBEAT_QUEUE)
    public void heartBeatStatus(byte[]  bytes, Message message, Channel channel) throws UnsupportedEncodingException {
        String encoded= new String(bytes,"UTF-8");
        MQResponseEntity mqResponseEntity= JsonUtils.jsonToPojo(encoded,MQResponseEntity.class);
        log.info("监听设备心跳队列：topic.heartbeat.queue，消息：" + JsonUtils.objectToJson(mqResponseEntity));
        Map<String, Object> map = JsonUtils.jsonToMap(mqResponseEntity.getData().toString());
        SmtClientManage smtClientManage = new SmtClientManage();
        smtClientManage.setClientId(Long.parseLong(map.get("clientId").toString()));
        smtClientManage.setSecretKey(mqResponseEntity.getKey());
        smtClientManage.setMonitoringTime(new Date());
        smtClientManage.setLoginTag((byte) 1);
        electronicTagFeignApi.updateClientManage(smtClientManage);

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            log.warn("===========手动确定消息队列：" + RabbitConfig.TOPIC_QUEUE1 + " 消息：" + message.getMessageProperties().getDeliveryTag() + "===============> " + JSONObject.toJSONString(mqResponseEntity));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
