package org.zewang.rabbitmqspringdemo1.service;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.zewang.rabbitmqspringdemo1.Config.DelayQueueConfig;
import org.zewang.rabbitmqspringdemo1.dto.OrderMessage;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/16 18:22
 */

@Service
public class DelayProducer {
    private final RabbitTemplate rabbitTemplate;
    public DelayProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDelay(OrderMessage orderMessage) {
        rabbitTemplate.convertAndSend(
            DelayQueueConfig.DELAY_EXCHANGE,
            DelayQueueConfig.ROUTING_DELAY,
            orderMessage
        );
        System.out.println("[Producer] 发送延迟消息： " + orderMessage.getProduct());
    }
}
