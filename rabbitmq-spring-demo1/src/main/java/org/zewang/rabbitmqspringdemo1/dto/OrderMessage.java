package org.zewang.rabbitmqspringdemo1.dto;


import java.io.Serializable;

/**
 * @author "Zewang"
 * @version 1.0
 * @description: TODO (这里用一句话描述这个类的作用)
 * @email "Zewang0217@outlook.com"
 * @date 2025/10/16 12:58
 */

// 实现序列化，才能发送到RabbitMQ
public class OrderMessage implements Serializable {

    private Long orderId;
    private String product;
    private Integer qty; // 购买数量

    public OrderMessage() {}

    public OrderMessage(Long orderId, String product, Integer qty) {
        this.orderId = orderId;
        this.product = product;
        this.qty = qty;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

}
