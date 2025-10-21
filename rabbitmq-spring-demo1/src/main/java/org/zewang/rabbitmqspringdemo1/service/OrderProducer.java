package org.zewang.rabbitmqspringdemo1.service;


import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.stereotype.Service;
import org.zewang.rabbitmqspringdemo1.Config.PriorityConfig;
import org.zewang.rabbitmqspringdemo1.Config.RabbitConfig;
import org.zewang.rabbitmqspringdemo1.dto.OrderMessage;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/16 13:28
 */

@Service
public class OrderProducer {
    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

        // publisher confirm callback
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->{
            if (ack) {
                log.info("消息已被 broker 确认， correlationId={}", correlationData != null ? correlationData.getId() : null);
            } else {
                log.warn("消息未被 broker 确认， cause={}", cause);
            }
        } );

        // returns callback ， 打那个mandatory=true并且路由失败是触发
        this.rabbitTemplate.setReturnsCallback(returned -> {
            log.error("消息路由失败， returned={}", returned);
        });
    }

    public void sendOrder(OrderMessage order) {
        String correlationId = UUID.randomUUID().toString();
        CorrelationData cd = new CorrelationData(correlationId);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, order, cd);
        log.info("发送订单消息：{}", order);
    }

    public void sendWithPriority(OrderMessage orderMessage, int priority) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
        };
        rabbitTemplate.convertAndSend(PriorityConfig.PRIORITY_EXCHANGE, PriorityConfig.PRIORITY_KEY, orderMessage,  messagePostProcessor);
        System.out.println("发送信息：" + orderMessage.getProduct() + ", 优先级：" + priority);
    }



}
