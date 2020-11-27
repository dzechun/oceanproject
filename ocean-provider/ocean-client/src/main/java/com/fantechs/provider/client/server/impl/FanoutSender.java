package com.fantechs.provider.client.server.impl;


import cn.hutool.extra.mail.Mail;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.entity.User;
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

    public void send(Object user) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE, RabbitConfig.TOPIC_TEXT, user);
    }

}
