package org.zewang.rabbitmqspringdemo1.listener;


import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.zewang.rabbitmqspringdemo1.Config.PriorityConfig;
import org.zewang.rabbitmqspringdemo1.dto.OrderMessage;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/16 18:57
 */

@Component
public class PriorityListener {
    @RabbitListener(queues = PriorityConfig.PRIORITY_QUEUE)
    public void process(OrderMessage orderMessage, Channel channel, Message message)
        throws IOException {
        try {
            // 获取消息的优先级属性
            Integer priority = message.getMessageProperties().getPriority();
            if (priority == null) {
                priority = 0; // 默认优先级为0
            }
            System.out.println("[Consumer] 收到优先级消息： " + orderMessage.getProduct() + "，优先级：" + priority + "，时间： " + System.currentTimeMillis());
            // 处理完成后的确认操作
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 处理失败时拒绝消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
