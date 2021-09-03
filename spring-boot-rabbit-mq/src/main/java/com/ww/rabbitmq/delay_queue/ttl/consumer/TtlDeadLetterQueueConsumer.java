package com.ww.rabbitmq.delay_queue.ttl.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: Sun
 * @create: 2021-09-03 14:16
 * @version: v1.0
 */
@Slf4j
@Component
public class TtlDeadLetterQueueConsumer {

    @RabbitListener(queues = "QUEUE_D")
    public void receiveD(Message message, Channel channel) {
        log.info("接收到的消息对象：" + message);

        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到死信队列信息：{}", new Date(), msg);
    }
}