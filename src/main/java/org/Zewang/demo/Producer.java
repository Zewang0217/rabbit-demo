package org.Zewang.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: 生产者
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/15 11:05
 */

public class Producer {
    // 队列名称
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");

        // 2. 创建连接
        Connection connection = factory.newConnection();

        // 3. 创建通道
        Channel channel = connection.createChannel();

        // 4. 声明队列 （如果不存在则创建） 参数说明：队列名称，是否持久化，是否独占，是否自动删除，参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 5. 发送消息
        String message = "Hello RabbitMQ!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [X] Sent '" + message + "'");

        // 6. 关闭连接
        channel.close();
        connection.close();
    }

}
