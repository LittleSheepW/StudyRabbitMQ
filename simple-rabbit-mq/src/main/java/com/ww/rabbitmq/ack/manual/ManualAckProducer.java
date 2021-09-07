package com.ww.rabbitmq.ack.manual;

import com.rabbitmq.client.Channel;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.util.Scanner;

/**
 * 为了确保消息永远不会丢失，RabbitMQ 支持消息确认。消费者发回一个确认消息，告诉 RabbitMQ 已经接收并处理了一条特定消息，并且 RabbitMQ 可以
 * 自由删除该消息。
 * 如果消费者在没有发送 ack 的情况下死亡(其通道关闭、连接关闭或 TCP 连接丢失) ，RabbitMQ 将理解消息没有被完全处理，并将重新对其排队。如果有
 * 其他消费者在线同时，它将迅速重新交付给另一个消费者。这样你就可以确保没有信息丢失，即使工人偶尔死亡。
 *
 * @author: Sun
 * @create: 2021-09-01 15:36
 * @version: v1.0
 */
public class ManualAckProducer {

    private static final String ACK_QUEUE_NAME = "manual_ack_queue";

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
            channel.queueDeclare(ACK_QUEUE_NAME, false, false, false, null);

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
                channel.basicPublish("", ACK_QUEUE_NAME, null, message.getBytes());
                System.out.println("发送消息完成:" + message);
            }
        }
    }
}