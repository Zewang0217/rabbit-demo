package org.Zewang.Ch3.demo_topic;


/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/15 18:55
 */
import com.rabbitmq.client.*;


public class TopicProducer {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String[] routingKeys = {
            "quick.orange.rabbit",
            "lazy.orange.elephant",
            "quick.brown.fox"
        };

        for (String routingKey : routingKeys) {
            String message = "Message with key: " + routingKey;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [X] Sent '" + message + "'");
        }

        channel.close();
        connection.close();
    }

}
