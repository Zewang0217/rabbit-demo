package org.zewang.rabbitmqspringdemo1.Config;


import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/16 18:43
 */

@Configuration
public class PriorityConfig {
    public static final String PRIORITY_EXCHANGE = "priority.exchange";
    public static final String PRIORITY_QUEUE = "priority.queue";
    public static final String PRIORITY_KEY = "priority.key";

    @Bean
    public Queue priorityQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        return QueueBuilder.durable(PRIORITY_QUEUE).withArguments(args).build();
    }

    @Bean
    public Exchange priorityExchange() {
        return ExchangeBuilder.directExchange(PRIORITY_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding priorityBinding() {
        return BindingBuilder.bind(priorityQueue())
            .to(priorityExchange())
            .with(PRIORITY_KEY).noargs();
    }

}
