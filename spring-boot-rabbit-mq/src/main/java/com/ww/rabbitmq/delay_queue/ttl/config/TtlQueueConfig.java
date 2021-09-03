package com.ww.rabbitmq.delay_queue.ttl.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Sun
 * @create: 2021-09-03 13:52
 * @version: v1.0
 */
@Configuration
public class TtlQueueConfig {

    public static final String X_EXCHANGE = "X";
    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_C = "QUEUE_C";

    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String DEAD_LETTER_QUEUE = "QUEUE_D";

    /**
     * 声明普通交换机X
     *
     * @return
     */
    @Bean("exchangeX")
    public DirectExchange exchangeX() {
        return new DirectExchange(X_EXCHANGE);
    }

    /**
     * 声明死信交换机Y
     *
     * @return
     */
    @Bean("exchangeY")
    public DirectExchange exchangeY() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明普通队列A：TTL为10秒、绑定死信交换机及路由键
     *
     * @return
     */
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", "YD");
        args.put("x-message-ttl", 10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
    }

    /**
     * 普通队列A与交换机X进行绑定
     *
     * @param queueA
     * @param exchangeX
     * @return
     */
    @Bean
    public Binding queueABindingExchangeX(@Qualifier("queueA") Queue queueA,
                                          @Qualifier("exchangeX") DirectExchange exchangeX) {
        return BindingBuilder.bind(queueA).to(exchangeX).with("XA");
    }

    /**
     * 声明普通队列B：TTL为40秒、绑定死信交换机及路由键
     *
     * @return
     */
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", "YD");
        args.put("x-message-ttl", 40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
    }

    /**
     * 普通队列B与交换机X进行绑定
     *
     * @param queueB
     * @param exchangeX
     * @return
     */
    @Bean
    public Binding queueBBindingExchangeX(@Qualifier("queueB") Queue queueB,
                                          @Qualifier("exchangeX") DirectExchange exchangeX) {
        return BindingBuilder.bind(queueB).to(exchangeX).with("XB");
    }

    /**
     * 声明普通队列C：不设置TTL、绑定死信交换机及路由键
     *
     * @return
     */
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", "YD");

        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    /**
     * 普通队列C与交换机X进行绑定
     *
     * @param queueC
     * @param exchangeX
     * @return
     */
    @Bean
    public Binding queuecBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("exchangeX") DirectExchange exchangeX) {
        return BindingBuilder.bind(queueC).to(exchangeX).with("XC");
    }

    /**
     * 声明死信队列D
     *
     * @return
     */
    @Bean("queueD")
    public Queue queueD() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    /**
     * 死信队列D与交换机Y进行绑定
     *
     * @param queueD
     * @param exchangeY
     * @return
     */
    @Bean
    public Binding queueDBindingExchangeY(@Qualifier("queueD") Queue queueD,
                                          @Qualifier("exchangeY") DirectExchange exchangeY) {
        return BindingBuilder.bind(queueD).to(exchangeY).with("YD");
    }
}