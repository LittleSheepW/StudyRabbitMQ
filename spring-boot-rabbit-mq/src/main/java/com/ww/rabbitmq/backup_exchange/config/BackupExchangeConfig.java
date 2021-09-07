package com.ww.rabbitmq.backup_exchange.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Sun
 * @create: 2021-09-06 15:11
 * @version: v1.0
 */
@Configuration
public class BackupExchangeConfig {

    public static final String NORMAL_EXCHANGE_NAME = "backup.normal.exchange";
    public static final String NORMAL_QUEUE_NAME = "backup.normal.queue";

    public static final String BACKUP_EXCHANGE_NAME = "backup.backup.exchange";
    public static final String BACKUP_QUEUE_NAME = "backup.backup.queue";

    public static final String WARNING_QUEUE_NAME = "warning.queue";

    @Bean("normalQueue")
    public Queue normalQueue() {
        return QueueBuilder.durable(NORMAL_QUEUE_NAME).build();
    }

    @Bean("normalExchange")
    public DirectExchange normalExchange() {
        // 设置该交换机的备份交换机
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange(NORMAL_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME);
        return exchangeBuilder.build();
    }

    @Bean
    public Binding queueBinding(@Qualifier("normalQueue") Queue queue,
                                @Qualifier("normalExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }


    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding warningBinding(@Qualifier("warningQueue") Queue queue,
                                  @Qualifier("backupExchange") FanoutExchange
                                          backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }

    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean
    public Binding backupBinding(@Qualifier("backupQueue") Queue queue,
                                 @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }
}