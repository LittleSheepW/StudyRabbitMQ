package com.ww.rabbitmq.backup_exchange.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: Sun
 * @create: 2021-09-06 15:24
 * @version: v1.0
 */
@Slf4j
@Component
public class BackupExchangeConsumer {

    public static final String NORMAL_QUEUE_NAME = "backup.normal.queue";
    public static final String BACKUP_QUEUE_NAME = "backup.backup.queue";

    @RabbitListener(queues = NORMAL_QUEUE_NAME)
    public void receiveNormalQueueMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("接受到队列 backup.normal.queue 消息：{}", msg);
    }

    @RabbitListener(queues = BACKUP_QUEUE_NAME)
    public void receiveBackupQueueMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("接受到队列 backup.backup.queue 消息：{}", msg);
    }
}
