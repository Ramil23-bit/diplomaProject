package org.telran.web.dto;

import org.telran.web.entity.OrderItems;

import java.util.List;

public class OrderCreateDto {

    private List<OrderItems> orderItems;
    private String deliveryAddress;
    private String deliveryMethod;

    public OrderCreateDto(List<OrderItems> orderItems, String deliveryAddress, String deliveryMethod) {
        this.orderItems = orderItems;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
}
