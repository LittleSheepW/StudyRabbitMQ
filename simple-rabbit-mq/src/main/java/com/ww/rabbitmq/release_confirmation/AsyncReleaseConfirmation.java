package com.ww.rabbitmq.release_confirmation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 异步发布确认：
 * 异步确认虽然编程逻辑比上两个要复杂，但是性价比最高，无论是可靠性还是效率都没得说，他是利用回调函数来达到消息可靠性传递的。
 *
 * @author: Sun
 * @create: 2021-09-02 10:55
 * @version: v1.0
 */
public class AsyncReleaseConfirmation {

    public static int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMQUtil.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null);
            // 开启发布确认
            channel.confirmSelect();

            /**
             * 线程安全有序的一个哈希表，适用于高并发的情况
             * 1.轻松的将序号与消息进行关联
             * 2.轻松批量删除条目 只要给到序列号
             * 3.支持并发访问
             */
            ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

            /**
             * 确认收到消息的回调
             * 1.消息序列号
             * 2.true 可以确认小于等于当前序列号的消息；false 确认当前序列号消息
             */
            ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
                System.out.println(Thread.currentThread().getName() + " 确认收到消息的回调：" + deliveryTag);
                if (multiple) {
                    // 返回的是小于等于当前序列号的未确认消息 是一个 map
                    ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag, true);
                    // 清除该部分未确认消息
                    confirmed.clear();
                } else {
                    // 只清除当前序列号的消息
                    outstandingConfirms.remove(deliveryTag);
                }
            };
            /**
             * 未收到消息的回调
             */
            ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
                String message = outstandingConfirms.get(deliveryTag);
                System.out.println("发布的消息" + message + "未被确认，序列号" + deliveryTag);
            };
            /**
             * 添加一个异步确认的监听器
             * 1.确认收到消息的回调
             * 2.未收到消息的回调
             */
            channel.addConfirmListener(ackCallback, null);

            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = "消息" + i;
                /**
                 * channel.getNextPublishSeqNo()获取下一个消息的序列号
                 * 通过序列号与消息体进行一个关联
                 * 全部都是未确认的消息体
                 */
                outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
                channel.basicPublish("", queueName, null, message.getBytes());
            }
            long end = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "发布" + MESSAGE_COUNT + "个异步确认消息,耗时" + (end - begin) + "ms");
        }
    }
}