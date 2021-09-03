package com.ww.rabbitmq.delay_queue.ttl.controller;

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
 * @create: 2021-09-03 14:14
 * @version: v1.0
 */
@Slf4j
@RestController
@RequestMapping("ttl")
public class TtlSendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试地址：http://localhost:8080/ttl/sendMsg/helloworld
     *
     * @param message
     * @return
     */
    @GetMapping("/sendMsg/{message}")
    public String sendMsg(@PathVariable String message) {
        log.info("当前时间：{}，发送一条信息给两个 TTL 队列：{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10S 的队列: " + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自 ttl 为 40S 的队列: " + message);

        return "消息发送成功";
    }

    /**
     * 测试地址：
     * http://localhost:8080/ttl/sendExpirationMsg/你好1/20000
     * http://localhost:8080/ttl/sendExpirationMsg/你好2/2000
     * <p>
     * 在消息属性上设置TTL，消息可能并不会按时“死亡“，因为 RabbitMQ 只会检查第一个消息是否过期，如果过期则丢到死信队列，如果第一个消息的延时
     * 时长很长，而第二个消息的延时时长很短，第二个消息并不会优先得到执行。
     *
     * @param message
     * @param ttlTime
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public String sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend("X", "XC", message,
                correlationData -> {
                    correlationData.getMessageProperties().setExpiration(ttlTime);
                    return correlationData;
                });
        log.info("当前时间：{}，发送一条时长{}毫秒 TTL 信息给队列 C：{}", new Date(), ttlTime, message);

        return "消息发送成功";
    }
}