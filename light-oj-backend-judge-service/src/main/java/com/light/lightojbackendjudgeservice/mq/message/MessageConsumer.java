package com.light.lightojbackendjudgeservice.mq.message;

import com.light.lightojbackendjudgeservice.service.JudgeService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author null&&
 * @Date 2024/7/8 22:13
 */
@Component
@Slf4j
public class MessageConsumer {

    @Resource
    private JudgeService judgeService;

    /**
     * 接收消息
     *
     * @param message     消息
     * @param channel     channel
     * @param deliveryTag 消息的deliveryTag
     */
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receive message: {}", message);
        long questionSubmitId = Long.parseLong(message);
        if (questionSubmitId <= 0) {
            channel.basicNack(deliveryTag, false, false);
            return;
        }
        try {
            judgeService.doJudge(questionSubmitId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
