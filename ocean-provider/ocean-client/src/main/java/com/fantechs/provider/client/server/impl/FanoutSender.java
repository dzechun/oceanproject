package com.fantechs.provider.client.server.impl;


import com.fantechs.provider.client.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lfz on 2020/11/27.
 */

@Service("fanoutSender")
public class FanoutSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(Object obj) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE, RabbitConfig.TOPIC_TEXT, obj);
    }

}
