package org.Zewang.Ch4.demo_ManualACK;


/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/15 19:13
 */
import com.rabbitmq.client.*;

public class ManualAckConsumer {
    private static final String QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明持久化队列
        boolean durable = true;
        // 参数说明：队列名称，是否持久化，是否独占，是否自动删除，额外参数（TTL，死信等）
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 一次只取一个消息（避免单消费者被压爆）
        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

            try {
                Thread.sleep(1000); // 模拟耗时处理
                System.out.println(" [x] Done");
                // 手动确认消息；参数说明：消息ID，是否批量
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.out.println(" [!] Error processing message: " + e.getMessage());
                // 手动拒绝重回队列；参数说明：消息ID，是否批量，是否重新入队
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
            }
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});

    }

}
