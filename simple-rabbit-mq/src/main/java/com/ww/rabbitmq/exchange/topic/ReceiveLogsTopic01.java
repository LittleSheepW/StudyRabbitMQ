package com.ww.rabbitmq.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author: Sun
 * @create: 2021-09-02 16:23
 * @version: v1.0
 */
public class ReceiveLogsTopic01 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");

        System.out.println("等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" 接收队列 :" + queueName + " 绑定键：" + delivery.getEnvelope().getRoutingKey() + ", 消息：" + message);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}