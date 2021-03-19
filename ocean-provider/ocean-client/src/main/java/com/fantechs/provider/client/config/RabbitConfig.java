package com.fantechs.provider.client.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lfz on 2020/11/27.
 */

@Configuration
public class RabbitConfig {
    //topic
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE_PDA = "topic.queue.pda";
    public static final String TOPIC_HEARTBEAT_QUEUE = "topic.heartbeat.queue";
    public static final String TOPIC_EXCHANGE = "topic.exchange";


    //fanout
    public static final String FANOUT_QUEUE1 = "fanout.queue1";
    public static final String FANOUT_QUEUE2 = "fanout.queue2";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    //redirect模式
    public static final String DIRECT_QUEUE1 = "direct.queue1";
    public static final String DIRECT_EXCHANGE = "direct.exchange";
    public static final String DIRECT_QUEUE2 = "direct.queue2";

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
        return new Queue(TOPIC_QUEUE1);
    }

    @Bean
    public Queue topicQueue2() {

//        Map<String, Object> map = new HashMap<>();
//        //绑定死信交换机
//        map.put("x-dead-letter-exchange", DEAD_EXCHANGE);
//        //绑定key
//        map.put("x-dead-letter-routing-key", DEAD_KEY);
//        //设置超时时间 3 ms
//        map.put("x-message-ttl", 1000 * 10);
//        //设置队列长度
//        map.put("x-max-length", 5);
//        //队列名 是否持久化 是否排他 是否自动删除 其他参数
//        return new Queue(TOPIC_HEARTBEAT_QUEUE, true, false, false, map);
        return new Queue(TOPIC_HEARTBEAT_QUEUE);
    }

//    @Bean
//    public Queue deadQueue() {
//        return new Queue(DEAD_QUEUE);
//    }

    @Bean
    public Queue topicQueuePda() {
        return new Queue(TOPIC_QUEUE_PDA);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

//    @Bean
//    public DirectExchange deadExchange() {
//        return new DirectExchange(DEAD_EXCHANGE);
//    }
//
//    @Bean
//    public Binding deadBinding() {
//        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_KEY);
//    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("lzc.message");
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("lzc.#");
    }

    @Bean
    public Binding topicBinding3() {
        return BindingBuilder.bind(topicQueuePda()).to(topicExchange()).with("lzc.#");
    }


    /**
     * Fanout模式
     * Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。
     *
     * @return
     */
    @Bean
    public Queue fanoutQueue1() {
        return new Queue(FANOUT_QUEUE1);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue(FANOUT_QUEUE2);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    /**
     * direct模式
     * 消息中的路由键（routing key）如果和 Binding 中的 binding key 一致， 交换器就将消息发到对应的队列中。路由键与队列名完全匹配
     *
     * @return
     */
    @Bean
    public Queue directQueue1() {
        return new Queue(DIRECT_QUEUE1);
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
