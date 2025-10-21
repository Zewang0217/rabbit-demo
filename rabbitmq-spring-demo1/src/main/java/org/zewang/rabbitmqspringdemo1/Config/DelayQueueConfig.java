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
 * @date 2025/10/16 18:14
 */
// 延迟队列
@Configuration
public class DelayQueueConfig {
    public static final String DELAY_EXCHANGE = "delay.exchange";
    public static final String DELAY_QUEUE = "delay.queue";
    public static final String PROCESS_QUEUE = "process.queue";
    public static final String ROUTING_DELAY = "delay.key";
    public static final String ROUTING_PROCESS = "process.key";

    @Bean
    public Exchange delayExchange() {
        return ExchangeBuilder.directExchange(DELAY_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 10000); // 10秒后过期
        args.put("x-dead-letter-exchange", DELAY_EXCHANGE);
        args.put("x-dead-letter-routing-key", ROUTING_PROCESS);
        return QueueBuilder.durable(DELAY_QUEUE).withArguments(args).build();
    }

    @Bean
    public Queue processQueue() {
        return QueueBuilder.durable(PROCESS_QUEUE).build();
    }

    @Bean
    public Binding bindDelay() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(ROUTING_DELAY).noargs();
    }

    @Bean
    public Binding bindProcess() {
        return BindingBuilder.bind(processQueue()).to(delayExchange()).with(ROUTING_PROCESS).noargs();
    }
}
