package org.zewang.rabbitmqspringdemo1.Config;


import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.MethodInvocationRecoverer;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/16 13:06
 */

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_NAME = "app.exchange";
    public static final String QUEUE = "app.quere.orders";
    public static final String ROUTING_KEY = "orders.key";

    // dead-letter 相关
    public static final String DLX_EXCHANGE = "app.dlx.exchange";
    public static final String DLX_QUEUE = "app.queue.orders.dlx";

    @Bean
    public Exchange appExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Queue appQueue() {
        Map<String, Object> args = new HashMap<>();
        // 失败后投递到 DLX
        args.put("x-dead-letter-exchange", DLX_EXCHANGE);
        args.put("x-dead-letter-routing-key", "dlx.orders");
        // 可选：消息过期时间（毫秒）或队列长度策略等
        return QueueBuilder.durable(QUEUE).withArguments(args).build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(appQueue()).to(appExchange()).with(ROUTING_KEY).noargs();
    }

    // DLX exchange & queue
    @Bean
    public Exchange dlxExchange() {
        return ExchangeBuilder.directExchange(DLX_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(DLX_QUEUE).build();
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("dlx.orders").noargs();
    }

    // JSON converter
    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate 调用 JSON converter, 并开启 mandatory(returns)
    @Bean
    public RabbitTemplate rabbitTemplate(
        ConnectionFactory connectionFactory, MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        template.setMandatory(true);
        return template;
    }

    // Listener container factory: 并发、手动 ack （可按需改为AUTO）
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
        ConnectionFactory cf, MessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        factory.setConcurrentConsumers(3); // 初始并发消费者数
        factory.setMaxConcurrentConsumers(10); // 最大并发
        factory.setPrefetchCount(10); // 每个 consumer 一次拉取的消息数
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);  // 手动 ack，适合精确控制重试

        return factory;
    }

}
