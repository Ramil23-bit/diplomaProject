package org.telran.web.dto;

import org.telran.web.entity.OrderItems;

import java.util.List;

public class OrderCreateDto {

    private Long userId;
    private List<OrderItemsCreateDto> orderItems;
    private String deliveryAddress;
    private String deliveryMethod;

    public OrderCreateDto(Long userId, List<OrderItemsCreateDto> orderItems, String deliveryAddress, String deliveryMethod) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
    }
    public OrderCreateDto() {
        //
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemsCreateDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemsCreateDto> orderItems) {
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
