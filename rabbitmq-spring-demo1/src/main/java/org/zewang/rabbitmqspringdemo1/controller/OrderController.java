package org.zewang.rabbitmqspringdemo1.controller;


import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zewang.rabbitmqspringdemo1.dto.OrderMessage;
import org.zewang.rabbitmqspringdemo1.service.DelayProducer;
import org.zewang.rabbitmqspringdemo1.service.OrderProducer;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/16 13:43
 */

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderProducer orderProducer;
    private final DelayProducer delayProducer;
    public OrderController(OrderProducer orderProducer, DelayProducer delayProducer) {
        this.orderProducer = orderProducer;
        this.delayProducer = delayProducer;
    }

    @PostMapping("/send")
    public String send(@RequestBody OrderMessage order) {
        for (int i = 0; i < 40; i++) {
            orderProducer.sendOrder(order);
        }
        return "sent";
    }

    @PostMapping("/send-delay")
    public String sendDelay(@RequestBody OrderMessage orderMessage) {
        delayProducer.sendDelay(orderMessage);
        return "delay-sent";
    }

    @PostMapping("/send-priority")
    public String sendWithPriority(@RequestBody OrderMessage orderMessage) {
        orderProducer.sendWithPriority(orderMessage, 4);
        return "priority-sent";
    }

}
