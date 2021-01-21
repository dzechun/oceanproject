package com.fantechs.provider.exhibition.service.impl;


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

    /**
     *
     * @param queueName 队列名
     * @param obj       消息
     */
    public void send(String queueName,Object obj) {
        this.rabbitTemplate.convertAndSend(queueName, obj);
    }

}
