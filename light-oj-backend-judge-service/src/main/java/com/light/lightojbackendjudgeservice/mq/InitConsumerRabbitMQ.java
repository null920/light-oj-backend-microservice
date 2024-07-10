package com.light.lightojbackendjudgeservice.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 初始化 RabbitMQ
 *
 * @author null&&
 * @Date 2024/7/8 19:24
 */
@Slf4j
@Component
public class InitConsumerRabbitMQ {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    private String EXCHANGE_NAME = "code_exchange";

    private String QUEUE_NAME = "code_queue";

    /**
     * 初始化 RabbitMQ
     */
    @PostConstruct
    public void doInit() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(host);
            connectionFactory.setUsername(username);
            connectionFactory.setPassword(password);
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "null_routingKey");
            log.info("初始化 RabbitMQ 成功");
        } catch (Exception e) {
            log.error("初始化 RabbitMQ 失败");
        }
    }
}
