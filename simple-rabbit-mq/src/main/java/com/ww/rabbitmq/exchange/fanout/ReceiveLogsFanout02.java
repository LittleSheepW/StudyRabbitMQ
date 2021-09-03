package com.ww.rabbitmq.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ww.rabbitmq.utils.RabbitMQUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author: Sun
 * @create: 2021-09-02 14:42
 * @version: v1.0
 */
public class ReceiveLogsFanout02 {
    private static final String EXCHANGE_NAME = "fanout_logs";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        // 把该临时队列绑定我们的 exchange 其中 routingkey(也称之为 binding key)为空字符串
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("等待接收消息，把接收到的消息写到文件.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            File file = new File("D:\\Devtools\\JavaProject\\StudyRabbitMQ\\src\\main\\java\\com\\ww\\rabbitmq\\exchange\\fanout\\rabbitmq_info.txt");
            FileUtils.writeStringToFile(file, message, "UTF-8", true);
            System.out.println("数据写入文件成功");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}