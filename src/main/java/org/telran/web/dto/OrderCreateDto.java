package org.telran.web.dto;

import java.util.List;

public class OrderCreateDto {
    private Long userId;
    private List<OrderItemsCreateDto> items;
    private String deliveryAddress;
    private String deliveryMethod;

    public OrderCreateDto(Long userId, List<OrderItemsCreateDto> items, String deliveryAddress, String deliveryMethod) {
        this.userId = userId;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
    }

    public OrderCreateDto() {
        //
    }

    public OrderCreateDto(List<OrderItemsCreateDto> items, String deliveryAddress, String deliveryMethod) {
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemsCreateDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemsCreateDto> items) {
        this.items = items;
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
