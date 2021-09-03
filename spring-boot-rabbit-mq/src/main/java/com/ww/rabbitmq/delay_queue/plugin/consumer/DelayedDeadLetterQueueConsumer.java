package com.ww.rabbitmq.delay_queue.plugin.consumer;

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
public class DelayedDeadLetterQueueConsumer {

    @RabbitListener(queues = "DELAYED_QUEUE")
    public void receiveDelayedQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到延时队列的消息：{}", new Date().toString(), msg);
    }
}