package com.ww.rabbitmq.message_fallback.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Sun
 * @create: 2021-09-06 14:45
 * @version: v1.0
 */
@Configuration
public class MessageFallbackConfig {

    public static final String FALLBACK_EXCHANGE_NAME = "fallback.exchange";
    public static final String FALLBACK_QUEUE_NAME = "fallback.queue";

    @Bean("fallbackExchange")
    public DirectExchange fallbackExchange() {
        return new DirectExchange(FALLBACK_EXCHANGE_NAME);
    }

    @Bean("fallbackQueue")
    public Queue fallbackQueue() {
        return QueueBuilder.durable(FALLBACK_QUEUE_NAME).build();
    }

    @Bean
    public Binding bindingFallbackQueue(@Qualifier("fallbackQueue") Queue queue,
                                        @Qualifier("fallbackExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }
}