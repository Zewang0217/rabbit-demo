package org.Zewang.Ch3.demo_topic;


/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/15 18:59
 */
import com.rabbitmq.client.*;

public class TopicConsumer {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();

        // 匹配所有包含 orange 的消息
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        // 匹配所有以 lazy 开头的消息
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");

        System.out.println(" [*] Waiting for topic logs...");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println(" [TopicConsumer] Received '" +
                delivery.getEnvelope().getRoutingKey() + ":" +
                new String(delivery.getBody(), "UTF-8") + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

    }

}
