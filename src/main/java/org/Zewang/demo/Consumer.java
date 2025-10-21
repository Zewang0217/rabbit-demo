package org.Zewang.demo;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/15 11:15
 */

public class Consumer {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");

        // 2. 建立连接
        Connection connection = factory.newConnection();

        // 3. 创建信道
        Channel channel = connection.createChannel();

        // 4. 声明队列 （必须与生产者一致）
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 5. 定义回调函数 （收到消息时调用）
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            // 从 RabbitMQ 消息中提取并解码消息内容为字符串
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [X] Received '" + message + "'");
        };

        // 6. 开始消费 （自动确认）
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }

}
