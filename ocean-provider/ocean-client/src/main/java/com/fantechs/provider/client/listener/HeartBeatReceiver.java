package com.fantechs.provider.client.listener;

import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class HeartBeatReceiver {

    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;

    // 监听设备心跳队列
    @RabbitListener(queues = RabbitConfig.TOPIC_HEARTBEAT_QUEUE)
    public void heartBeatStatus(byte[]  bytes) throws UnsupportedEncodingException {
        String encoded= new String(bytes,"UTF-8");
        MQResponseEntity mqResponseEntity= JsonUtils.jsonToPojo(encoded,MQResponseEntity.class);
        SmtClientManage smtClientManage = new SmtClientManage();
        smtClientManage.setSecretKey(mqResponseEntity.getKey());
        smtClientManage.setMonitoringTime(new Date());
        smtClientManage.setLoginTag((byte) 1);
        electronicTagFeignApi.updateClientManage(smtClientManage);
    }


}
