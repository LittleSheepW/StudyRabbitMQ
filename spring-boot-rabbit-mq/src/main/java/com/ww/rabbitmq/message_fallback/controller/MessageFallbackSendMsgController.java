package com.ww.rabbitmq.message_fallback.controller;

import com.ww.rabbitmq.common.callback.MyCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author: Sun
 * @create: 2021-09-06 14:28
 * @version: v1.0
 */
@Slf4j
@RestController
@RequestMapping("/fallback")
public class MessageFallbackSendMsgController {

    @Autowired
    private MyCallback myCallback;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("fallback.exchange", "key1", message + "key1", correlationData1);
        log.info("发送消息 id 为：{}内容为{}", correlationData1.getId(), message + "key1");

        CorrelationData correlationData2 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("fallback.exchange", "key2", message + "key2", correlationData2);
        log.info("发送消息 id 为：{}内容为{}", correlationData2.getId(), message + "key2");
    }

    /**
     * 依赖注入 rabbitTemplate 之后再设置它的回调对象
     */
    @PostConstruct
    private void init() {
        rabbitTemplate.setConfirmCallback(myCallback);

        // true：交换机无法将消息进行路由时，会将该消息返回给生产者；false：如果发现消息无法进行路由，则直接丢弃
        rabbitTemplate.setMandatory(true);
        // 设置回退消息交给谁处理
        rabbitTemplate.setReturnCallback(myCallback);
    }
}