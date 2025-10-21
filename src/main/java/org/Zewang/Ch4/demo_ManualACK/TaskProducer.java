package org.Zewang.Ch4.demo_ManualACK;


/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/15 23:13
 */
import com.rabbitmq.client.*;


public class TaskProducer {
    private static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 持久化队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 发送信息
        for (int i = 1; i <= 5; i++) {
            String message = "Task #" + i;
            // 消息持久化属性
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)// 表示持久化消息
                .build();

            channel.basicPublish("", QUEUE_NAME, props, message.getBytes("UTF-8"));
            System.out.println(" [X] Sent '" + message + "'");
        }

        channel.close();
        connection.close();
    }

}
