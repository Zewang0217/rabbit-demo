package org.zewang.rabbitmqspringdemo1.listener;


import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.sql.Time;
import java.util.Timer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.zewang.rabbitmqspringdemo1.Config.DelayQueueConfig;
import org.zewang.rabbitmqspringdemo1.dto.OrderMessage;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/16 18:25
 */

@Component
public class DelayListener {
    @RabbitListener(queues = DelayQueueConfig.PROCESS_QUEUE)
    public void process(OrderMessage orderMessage, Channel channel, Message message)
        throws IOException {
        try {
            System.out.println("[Consumer] 收到延迟消息： " + orderMessage.getProduct() + "时间： " + System.currentTimeMillis());
            // 处理完成后手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 处理失败时拒绝消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

}
