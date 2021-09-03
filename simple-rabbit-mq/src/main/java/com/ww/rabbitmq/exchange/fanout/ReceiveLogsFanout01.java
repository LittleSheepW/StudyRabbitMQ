package com.ww.rabbitmq.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author: Sun
 * @create: 2021-09-02 14:42
 * @version: v1.0
 */
public class ReceiveLogsFanout01 {
    private static final String EXCHANGE_NAME = "fanout_logs";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        // 把该临时队列绑定我们的 exchange 其中 routingkey(也称之为 binding key)为空字符串
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("等待接收消息，把接收到的消息打印在屏幕.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("控制台打印接收到的消息：" + message);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}