package com.ww.rabbitmq.hello_world;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author: Sun
 * @create: 2021-09-01 11:25
 * @version: v1.0
 */
public class Producer {

    private final static String QUEUE_NAME = "hello_world";

    public static void main(String[] args) {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("123456");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            /**
             * 声明队列
             * queue – 队列的名称
             * durable - 如果我们声明一个持久队列，则为true（该队列将在服务器重启后继续存在）
             * exclusive – 如果我们声明独占队列（仅限于此连接），则为 true
             * autoDelete – 如果我们声明一个自动删除队列，则为 true（服务器将在不再使用时将其删除）
             * arguments - 队列的其他属性（构造参数）
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            /**
             * 发送消息
             * exchange - 要将消息发布到的Exchange
             * routingKey – 路由密钥
             * props - 消息的其他属性 - 路由标头等
             * body – 消息正文
             */
            String message = UUID.randomUUID().toString();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完毕");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}