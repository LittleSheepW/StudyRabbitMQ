package com.ww.rabbitmq.ack.manual;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ww.rabbitmq.utils.RabbitMQUtil;
import com.ww.rabbitmq.utils.SleepUtil;

/**
 * @author: Sun
 * @create: 2021-09-01 17:03
 * @version: v1.0
 */
public class ManualAckWorker01 {

    private static final String ACK_QUEUE_NAME = "manual_ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("Worker01 等待接收消息处理时间较短");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            SleepUtil.sleep(1);

            String receivedMessage = new String(delivery.getBody());
            System.out.println("Worker01 接收到消息:" + receivedMessage);

            /**
             * deliveryTag – 来自收到的AMQP.Basic.GetOk或AMQP.Basic.Deliver的标签
             * multiple – 如果为true，则确认包括提供的传递标记在内的所有消息；如果为false，则仅确认提供的传递标记。
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        /**
         * 消费消息(手动应答)
         * queue – 队列的名称
         * autoAck – 如果服务器应该考虑一旦传递就确认消息，则为 true；如果服务器应该期待明确的确认，则为 false
         * DeliverCallback – 消息投递时的回调
         * cancelCallback – 消费者取消时的回调
         */
        channel.basicConsume(ACK_QUEUE_NAME, false, deliverCallback, (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        });
    }
}
