package com.ww.rabbitmq.dead_letter_queue.refuse;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Sun
 * @create: 2021-09-02 18:04
 * @version: v1.0
 */
public class RefuseConsumer01 {

    private static final String NORMAL_EXCHANGE = "refuse_normal_exchange";
    private static final String DEAD_EXCHANGE = "refuse_dead_exchange";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        // 声明死信交换机及死信队列并进行绑定
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        String deadQueue = "refuse-dead-queue";
        channel.queueDeclare(deadQueue, false, false, false, null);
        channel.queueBind(deadQueue, DEAD_EXCHANGE, "dead");

        // 正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        params.put("x-dead-letter-routing-key", "dead");
        // 声明普通交换机及普通队列并进行绑定
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        String normalQueue = "refuse-normal-queue";
        channel.queueDeclare(normalQueue, false, false, false, params);
        channel.queueBind(normalQueue, NORMAL_EXCHANGE, "normal");

        System.out.println("等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            if ("info5".equals(message)) {
                System.out.println("Consumer01 接收到消息" + message + "并拒绝签收该消息");
                // requeue 设置为 false 代表拒绝重新入队，该队列如果配置了死信交换机将发送到死信队列中
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01 接收到消息" + message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(normalQueue, false, deliverCallback, consumerTag -> {
        });
    }
}