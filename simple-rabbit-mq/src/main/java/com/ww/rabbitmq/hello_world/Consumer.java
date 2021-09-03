package com.ww.rabbitmq.hello_world;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: Sun
 * @create: 2021-09-01 11:53
 * @version: v1.0
 */
public class Consumer {

    private final static String QUEUE_NAME = "hello_world";

    /**
     * 推送的消息如何进行消费的接口回调
     */
    static DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody());
        System.out.println(message);
    };
    /**
     * 取消消费的一个回调接口 如在消费的时候队列被删除掉了
     */
    static CancelCallback cancelCallback = (consumerTag) -> {
        System.out.println("消息消费被中断");
    };

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println("等待接收消息....");

        /**
         * 消费消息
         * queue – 队列的名称
         * autoAck – 如果服务器应该考虑一旦传递就确认消息，则为 true；如果服务器应该期待明确的确认，则为 false
         * DeliverCallback – 消息投递时的回调
         * cancelCallback – 消费者取消时的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}