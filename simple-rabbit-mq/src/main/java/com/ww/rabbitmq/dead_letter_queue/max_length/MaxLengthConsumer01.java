package com.ww.rabbitmq.dead_letter_queue.max_length;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Sun
 * @create: 2021-09-02 17:13
 * @version: v1.0
 */
public class MaxLengthConsumer01 {

    private static final String NORMAL_EXCHANGE = "max_length_normal_exchange";
    private static final String DEAD_EXCHANGE = "max_length_dead_exchange";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        // 声明死信交换机及死信队列并进行绑定
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        String deadQueue = "max-length-dead-queue";
        channel.queueDeclare(deadQueue, false, false, false, null);
        channel.queueBind(deadQueue, DEAD_EXCHANGE, "dead");

        // 正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        params.put("x-dead-letter-routing-key", "dead");
        params.put("x-max-length", 6);
        // 声明普通交换机及普通队列并进行绑定
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        String normalQueue = "max-length-normal-queue";
        channel.queueDeclare(normalQueue, false, false, false, params);
        channel.queueBind(normalQueue, NORMAL_EXCHANGE, "normal");

        System.out.println("等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Consumer01 接收到消息：" + message);
        };
        channel.basicConsume(normalQueue, true, deliverCallback, consumerTag -> {
        });
    }
}