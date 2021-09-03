package com.ww.rabbitmq.dead_letter_queue.refuse;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.ww.rabbitmq.utils.RabbitMQUtil;

/**
 * 消息被拒绝后自动将消息转发到死信队列中。
 *
 * @author: Sun
 * @create: 2021-09-02 17:12
 * @version: v1.0
 */
public class RefuseProducer {

    private static final String NORMAL_EXCHANGE = "refuse_normal_exchange";

    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMQUtil.getChannel()) {
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

            // 该信息是用作演示队列个数限制
            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                channel.basicPublish(NORMAL_EXCHANGE, "normal", null, message.getBytes());
                System.out.println("生产者发送消息:" + message);
            }
        }
    }
}