package com.ww.rabbitmq.common.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: Sun
 * @create: 2021-09-06 11:49
 * @version: v1.0
 */
@Slf4j
@Component
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    /**
     * 交换机不管是否收到消息都会回调该访问
     *
     * @param correlationData 回调的相关数据
     * @param ack             ack 为真，nack 为假
     * @param cause           可选原因，如果可用，则为 nack，否则为 null。
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到 id 为：{}的消息", id);
        } else {
            log.info("交换机还未收到 id 为：{}消息,由于原因：{}", id, cause);
        }
    }

    /**
     * 当消息无法路由时的回调方法
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error(" 消 息 {}, 被交换机 {} 退回，退回原因：{}, 路 由 key:{}", new String(message.getBody()), exchange,
                replyText, routingKey);
    }
}
