package com.fantechs.provider.exhibition.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lfz on 2020/11/27.
 */

@Configuration
public class RabbitConfig {
    //topic
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_HEARTBEAT_QUEUE = "topic.heartbeat.queue";
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String TOPIC_IMAGE_QUEUE = "topic.image.queue";
    public static final String TOPIC_WORK_QUEUE = "topic.work.queue";
    public static final String TOPIC_LISTENER_WORK_QUEUE = "topic.listener.work.queue";
    public static final String TOPIC_PROCESS_LIST_QUEUE = "topic.process.list.queue";
    public static final String TOPIC_PROCESS_WORK_QUEUE = "topic.process.work.queue";



    //fanout
    public static final String FANOUT_QUEUE1 = "fanout.queue1";
    public static final String FANOUT_QUEUE2 = "fanout.queue2";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    //redirect模式
    public static final String DIRECT_QUEUE1 = "direct.queue1";
    public static final String DIRECT_EXCHANGE = "direct.exchange";
    public static final String DIRECT_QUEUE2 ="direct.queue2" ;
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
        return new Queue(TOPIC_HEARTBEAT_QUEUE);
    }

    @Bean
    public Queue topicProcessListQueue() {
        return new Queue(TOPIC_PROCESS_LIST_QUEUE);
    }

    @Bean
    public Queue topicImageQueue() {
        return new Queue(TOPIC_IMAGE_QUEUE);
    }

    @Bean
    public Queue topicWorkQueue() {
        return new Queue(TOPIC_WORK_QUEUE);
    }

    @Bean
    public Queue topicListenerWorkQueue() {
        return new Queue(TOPIC_LISTENER_WORK_QUEUE);
    }

    @Bean
    public Queue topicProcessWorkQueue() {
        return new Queue(TOPIC_PROCESS_WORK_QUEUE);
    }


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1() {   return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("lzc.message"); }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("lzc.#");
    }

    @Bean
    public Binding topicBinding3() {
        return BindingBuilder.bind(topicProcessWorkQueue()).to(topicExchange()).with("lzc.#");
    }


    /**
     * Fanout模式
     * Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。
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
