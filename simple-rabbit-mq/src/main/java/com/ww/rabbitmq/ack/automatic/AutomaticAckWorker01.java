package com.ww.rabbitmq.ack.automatic;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ww.rabbitmq.utils.RabbitMQUtil;

/**
 * @author: Sun
 * @create: 2021-09-01 15:35
 * @version: v1.0
 */
public class AutomaticAckWorker01 {

    private static final String QUEUE_NAME = "automatic_ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String receivedMessage = new String(delivery.getBody());
            System.out.println("Worker01 接收到消息:" + receivedMessage);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        };
        System.out.println("Worker01 消费者启动等待消费......");

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