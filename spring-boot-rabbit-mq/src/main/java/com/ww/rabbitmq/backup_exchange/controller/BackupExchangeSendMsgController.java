package com.ww.rabbitmq.backup_exchange.controller;

import com.ww.rabbitmq.common.callback.MyCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author: Sun
 * @create: 2021-09-06 11:47
 * @version: v1.0
 */
@Slf4j
@RestController
@RequestMapping("/backup")
public class BackupExchangeSendMsgController {

    public static final String NORMAL_EXCHANGE_NAME = "backup.normal.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MyCallback myCallback;

    @GetMapping("/sendMessage/{message}")
    public String sendMessage(@PathVariable String message) {
        String routingKey1 = "key1";
        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE_NAME, routingKey1, message, correlationData1);

        String routingKey2 = "key2";
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE_NAME, routingKey2, message, correlationData2);
        log.info("发送消息内容：{}", message);

        return "消息发送成功";
    }

    /**
     * 依赖注入 rabbitTemplate 之后再设置它的回调对象
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myCallback);
    }
}