package com.ww.rabbitmq.delay_queue.plugin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: Sun
 * @create: 2021-09-03 15:42
 * @version: v1.0
 */
@Slf4j
@RestController
@RequestMapping("delay")
public class DelayedSendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试地址：
     * http://localhost:8080/delay/sendDelayMsg/hello/20000
     * http://localhost:8080/delay/sendDelayMsg/world/2000
     *
     * @param message
     * @param delayTime
     */
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public String sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend("DELAYED_EXCHANGE", "DELAYED_ROUTING_KEY", message,
                correlationData -> {
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
        log.info(" 当 前 时 间 ： {}，发送一条延迟 {} 毫秒的信息给队列 DELAYED_QUEUE：{}", new Date(), delayTime, message);

        return "消息发送成功";
    }
}