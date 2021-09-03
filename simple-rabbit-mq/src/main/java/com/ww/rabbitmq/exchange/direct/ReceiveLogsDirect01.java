package com.ww.rabbitmq.exchange.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ww.rabbitmq.utils.RabbitMQUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author: Sun
 * @create: 2021-09-02 15:33
 * @version: v1.0
 */
public class ReceiveLogsDirect01 {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = "disk";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "error");

        System.out.println("等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            message = "接收绑定键:" + delivery.getEnvelope().getRoutingKey() + "，消息：" + message;
            File file = new File("D:\\Devtools\\JavaProject\\StudyRabbitMQ\\src\\main\\java\\com\\ww\\rabbitmq\\exchange\\direct\\rabbitmq_info.txt");
            FileUtils.writeStringToFile(file, message, "UTF-8", true);
            System.out.println("错误日志已经接收");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}