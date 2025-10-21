package org.Zewang.Ch4.demo_publisherConfirm;


/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/15 23:25
 */
import com.rabbitmq.client.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;


public class PublisherConfirmProducer {
    private static final String QUEUE_NAME = "confirm_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false,false, null);

        // 启用确认模式
        channel.confirmSelect();

        // 记录未确认消息  键是消息序列号 seqNo，值是消息内容   注：这个数据结构是线程安全的，且支持高效的范围操作
        ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        // 成功回调
        ConfirmCallback ackCallback = (sequenceNumber, multiple) -> { // 参数说明：消息序列号，是否批量
            if (multiple) {
                outstandingConfirms.headMap(sequenceNumber, true).clear(); // 删除小于等于该序列号的所有未确认消息
            } else {
                outstandingConfirms.remove(sequenceNumber); // 删除该序列号对应的未确认消息
            }
            System.out.println(" [R] Confirmed message " + sequenceNumber);
        };

        // 失败回调
        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> { // 参数说明：消息序列号，是否批量
            String body = outstandingConfirms.get(sequenceNumber); // 获取未确认消息
            System.err.println(" [X] Message " + sequenceNumber + " failed: " + body); // 打印未确认消息内容
        };

        channel.addConfirmListener(ackCallback, nackCallback); // 同时监听成功和失败的消息确认回调

        // 批量发送消息
        for (int i = 1; i <= 10; i++) {
            String message = "Confirm message #" + i;
            long seqNo = channel.getNextPublishSeqNo();
            outstandingConfirms.put(seqNo, message); // 先放入未确认信息列表，如果确认回调函数会自动删除
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8")); // 发送消息
            Thread.sleep(1000); // 等待控制台打印
        }

        System.out.println(" [x] All messages sent, waiting for confirms...");

        System.out.println(" [x] All confirmed");

        channel.close();
        connection.close();
    }

}
