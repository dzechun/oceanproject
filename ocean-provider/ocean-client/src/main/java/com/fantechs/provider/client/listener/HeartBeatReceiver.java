package com.fantechs.provider.client.listener;

import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HeartBeatReceiver {

    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;

    // 监听设备心跳队列
    @RabbitListener(queues = RabbitConfig.TOPIC_HEARTBEAT_QUEUE)
    public void heartBeatStatus(SmtClientManage smtClientManage) {
        smtClientManage.setMonitoringTime(new Date());
        smtClientManage.setLoginTag((byte) 1);
        electronicTagFeignApi.updateClientManage(smtClientManage);
    }


}
