package com.ww.rabbitmq.release_confirmation;

import com.rabbitmq.client.Channel;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.util.UUID;

/**
 * 批量确认发布：
 * 与单个等待确认消息相比，先发布一批消息然后一起确认可以极大地提高吞吐量，当然这种方式的缺点就是:当发生故障导致发布出现问题时，不知道是哪个消息
 * 出现问题了，我们必须将整个批处理保存在内存中，以记录重要的信息而后重新发布消息。当然这种方案仍然是同步的，也一样阻塞消息的发布。
 *
 * @author: Sun
 * @create: 2021-09-02 10:48
 * @version: v1.0
 */
public class BatchReleaseConfirmation {

    public static int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMQUtil.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null);
            // 开启发布确认
            channel.confirmSelect();
            // 批量确认消息大小
            int batchSize = 100;
            // 未确认消息个数
            int outstandingMessageCount = 0;

            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                channel.basicPublish("", queueName, null, message.getBytes());
                outstandingMessageCount++;
                if (outstandingMessageCount == batchSize) {
                    System.out.println("开始批量确认发布");
                    channel.waitForConfirms(1000);
                    System.out.println("批量确认发布成功");
                    outstandingMessageCount = 0;
                }
            }

            // 为了确保还有剩余没有确认消息 再次确认
            if (outstandingMessageCount > 0) {
                channel.waitForConfirms(1000);
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) + "ms");
        }
    }
}