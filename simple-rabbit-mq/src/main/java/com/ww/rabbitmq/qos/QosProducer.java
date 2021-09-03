package com.ww.rabbitmq.qos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.ww.rabbitmq.utils.RabbitMQUtil;

import java.util.Scanner;

/**
 * 您可能已经注意到，调度仍然不能完全按照我们的要求工作。例如，在有两个工人的情况下，当所有单数消息都很重，而双数消息都很轻时，一个工人会经常忙碌，
 * 而另一个工人几乎不会做任何工作。好吧，RabbitMQ对此一无所知，仍然会均匀地发送消息。
 * RabbitMQ只在消息进入队列时调度消息。它不会查看消费者的未确认消息数量。它只是盲目地将每第n条消息分派给第n个消费者。
 * 为了克服这一点，我们可以使用带有prefetchCount=1设置的basicQos方法。这告诉RabbitMQ不要一次向一个工人发送多条消息。或者，换句话说，在工人
 * 处理并确认前一条消息之前，不要向其发送新消息。相反，它会将其分派给下一个不忙的员工。
 *
 * 注意：如果所有的工作人员都很忙，您的队列可能会被填满。你会想要密切关注这一点，也许会增加更多的员工，或者有一些其他的战略。
 *
 * @author: Sun
 * @create: 2021-09-01 15:36
 * @version: v1.0
 */
public class QosProducer {

    private static final String ACK_QUEUE_NAME = "qos_queue";

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMQUtil.getChannel()) {
            /**
             * 声明队列
             * queue – 队列的名称
             * durable - 如果我们声明一个持久队列，则为true（该队列将在服务器重启后继续存在）
             * exclusive – 如果我们声明独占队列（仅限于此连接），则为 true
             * autoDelete – 如果我们声明一个自动删除队列，则为 true（服务器将在不再使用时将其删除）
             * arguments - 队列的其他属性（构造参数）
             */
            channel.queueDeclare(ACK_QUEUE_NAME, true, false, false, null);

            System.out.println("请输入信息：");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                /**
                 * 发送消息
                 * exchange - 要将消息发布到的Exchange
                 * routingKey – 路由密钥
                 * props - 消息的其他属性 - 路由标头等
                 * body – 消息正文
                 */
                channel.basicPublish("", ACK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println("发送消息完成:" + message);
            }
        }
    }
}