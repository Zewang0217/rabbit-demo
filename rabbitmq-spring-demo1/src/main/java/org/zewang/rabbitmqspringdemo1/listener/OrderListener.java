package org.zewang.rabbitmqspringdemo1.listener;


import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.zewang.rabbitmqspringdemo1.Config.RabbitConfig;
import org.zewang.rabbitmqspringdemo1.dto.OrderMessage;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/16 13:35
 */

@Component
public class OrderListener {
    private static final Logger log = LoggerFactory.getLogger(OrderListener.class);

    @RabbitListener(queues = RabbitConfig.QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(OrderMessage order, Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info(Thread.currentThread().getName()+"收到订单，处理中：{}", order);
            // 模拟业务处理
            if (order.getQty() < 0) {
                throw new IllegalArgumentException("数量异常");
            }
            // 业务成功收到 ack  参数说明：消息ID，是否批量
            channel.basicAck(deliveryTag, false);
        } catch (Exception ex) {
            log.error(Thread.currentThread().getName()+"订单处理失败：{}, msg: {}", order, ex.getMessage());
            // 可以选择重试 （NACK requeue=true), 后直接nack丢到DLX （requeue=false）
            boolean requeue = false;
            channel.basicNack(deliveryTag, false, requeue);
        }
    }

}
