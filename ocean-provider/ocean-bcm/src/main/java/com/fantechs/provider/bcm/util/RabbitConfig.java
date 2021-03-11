package com.fantechs.provider.bcm.util;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.Lei
 * @create 2021/2/24
 */
//@Configuration
public class RabbitConfig {
    /**
     * 定义demoQueue队列
     * @return
     */
    //@Bean
    public Queue demoString() {
        return new Queue("demoQueue");
    }
}
