package com.ww.rabbitmq.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author: Sun
 * @create: 2021-09-02 14:41
 * @version: v1.0
 */
public class EmitLogFanout {
    private static final String EXCHANGE_NAME = "fanout_logs";

    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMQUtil.getChannel()) {
            /**
             * 声明一个 exchange
             * 1.exchange 的名称
             * 2.exchange 的类型
             */
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入信息");
            while (sc.hasNext()) {
                String message = sc.nextLine();
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("生产者发出消息" + message);
            }
        }
    }
}