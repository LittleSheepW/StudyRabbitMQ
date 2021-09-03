package com.ww.rabbitmq.delay_queue.plugin.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Sun
 * @create: 2021-09-03 15:35
 * @version: v1.0
 */
@Configuration
public class DelayedQueueConfig {

    public static final String DELAYED_QUEUE_NAME = "DELAYED_QUEUE";
    public static final String DELAYED_EXCHANGE_NAME = "DELAYED_EXCHANGE";
    public static final String DELAYED_ROUTING_KEY = "DELAYED_ROUTING_KEY";

    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    /**
     * 自定义交换机，这里定义的是一个延迟交换机
     *
     * @return
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");

        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    /**
     * 队列与交换机进行绑定
     *
     * @param queue
     * @param delayedExchange
     * @return
     */
    @Bean
    public Binding bindingDelayedQueue(@Qualifier("delayedQueue") Queue queue,
                                       @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(queue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}