package com.ww.rabbitmq.message_fallback.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: Sun
 * @create: 2021-09-06 11:52
 * @version: v1.0
 */
@Slf4j
@Component
public class MessageFallbackConsumer {

    public static final String CONFIRM_QUEUE_NAME = "fallback.queue";

    @RabbitListener(queues = CONFIRM_QUEUE_NAME)
    public void receiveMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("接受到队列 confirm.queue 消息：{}", msg);
    }
}