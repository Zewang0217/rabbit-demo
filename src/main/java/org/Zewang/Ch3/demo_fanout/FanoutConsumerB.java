package org.Zewang.Ch3.demo_fanout;


/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/15 18:32
 */
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanoutConsumerB {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String queueName = channel.queueDeclare().getQueue();
        // 参数说明: 队列名称, 交换机名称, 路由键
        channel.queueBind(queueName, EXCHANGE_NAME, "fanout");

        System.out.println(" [A] Waiting for messages...");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println(" [A] Received: " + new String(delivery.getBody(), "UTF-8"));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});


    }

}
