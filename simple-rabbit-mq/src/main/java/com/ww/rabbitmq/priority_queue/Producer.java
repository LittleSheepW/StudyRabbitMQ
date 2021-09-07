package com.ww.rabbitmq.priority_queue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.ww.rabbitmq.utils.RabbitMQUtil;

/**
 * @author: Sun
 * @create: 2021-09-06 16:11
 * @version: v1.0
 */
public class Producer {

    private static final String QUEUE_NAME = "priority_queue";

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMQUtil.getChannel()) {
            // 给消息赋予一个 priority 属性
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                if (i == 5) {
                    channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
                } else {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                }
                System.out.println("发送消息完成:" + message);
            }
        }
    }
}