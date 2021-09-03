package com.ww.rabbitmq.persistence;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.util.Scanner;

/**
 * 当 RabbitMQ 退出或崩溃时，它将忘记队列和消息，除非您告诉它不要这样做。要确保消息不丢失，需要做两件事: 我们需要将队列和消息都标记为持久性。
 *
 * @author: Sun
 * @create: 2021-09-01 15:36
 * @version: v1.0
 */
public class PersistenceProducer {

    private static final String ACK_QUEUE_NAME = "persistence_queue";

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMQUtil.getChannel()) {
            /**
             * 声明队列
             * queue – 队列的名称
             * durable - 如果我们声明一个持久队列，则为true（该队列将在服务器重启后继续存在）
             * exclusive – 如果我们声明独占队列（仅限于此连接），则为 true
             * autoDelete – 如果我们声明一个自动删除队列，则为 true（服务器将在不再使用时将其删除）
             * arguments - 队列的其他属性（构造参数）
             */
            channel.queueDeclare(ACK_QUEUE_NAME, true, false, false, null);

            System.out.println("请输入信息：");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                /**
                 * 发送消息
                 * exchange - 要将消息发布到的Exchange
                 * routingKey – 路由密钥
                 * props - 消息的其他属性 - 路由标头等
                 * body – 消息正文
                 */
                channel.basicPublish("", ACK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println("发送消息完成:" + message);
            }
        }
    }
}