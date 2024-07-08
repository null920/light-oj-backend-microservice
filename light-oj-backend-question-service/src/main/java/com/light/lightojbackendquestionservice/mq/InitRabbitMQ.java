package com.light.lightojbackendquestionservice.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化 RabbitMQ
 *
 * @author null&&
 * @Date 2024/7/8 19:24
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Data
public class InitRabbitMQ implements ApplicationRunner {

    private String host;

    private String username;

    private String password;

    private String EXCHANGE_NAME = "code_exchange";

    private String QUEUE_NAME = "code_queue";

    /**
     * 程序启动后执行
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        doInit();
    }

    /**
     * 初始化 RabbitMQ
     */
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
