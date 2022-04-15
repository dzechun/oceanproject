package com.fantechs.provider.mes.sfc.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lfz on 2020/11/27.
 */

@Configuration
public class RabbitConfig {
    //topic
    public static final String QUEUE_NAME_FILE = "topic.prinQueue";
    // 万宝项目-对接PLC队列key
    public static final String STACKING_QUEUE_NAME = "wanbaostackingQueue";
    //redirect模式
    public static final String DIRECT_QUEUE1 = "direct.queue1";
    public static final String DIRECT_EXCHANGE = "direct.exchange";
    public static final String DIRECT_QUEUE2 = "direct.queue2";

    public static final String TOPIC_EXCHANGE="topic.exchange";

//    //死信队列名称
//    public static final String DEAD_QUEUE = "dead.queue";
//    //死信交换机名称
//    public static final String DEAD_EXCHANGE = "dead.exchange";
//    //死信交换机 key
//    public static final String DEAD_KEY = "dead.key";

    /**
     * Topic模式
     *
     * @return
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(QUEUE_NAME_FILE);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("lzc.message");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory cachingConnectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory);
        rabbitAdmin.setIgnoreDeclarationExceptions(true);
        return rabbitAdmin;
    }

    /**
     * direct模式
     * 消息中的路由键（routing key）如果和 Binding 中的 binding key 一致， 交换器就将消息发到对应的队列中。路由键与队列名完全匹配
     *
     * @return
     */
    @Bean
    public Queue directQueue1() {
        return new Queue(QUEUE_NAME_FILE);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding directBinding1() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("direct.pwl");
    }

}
