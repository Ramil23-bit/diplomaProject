package org.telran.web.dto;

import org.telran.web.entity.OrderItems;
import org.telran.web.entity.User;
import org.telran.web.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private Long id;
    private User user;
    private List<OrderItems> orderItems;
    private LocalDateTime createdAt;
    private String deliveryAddress;
    private String contactPhone;
    private String deliveryMethod;
    private OrderStatus status;
    private LocalDateTime updatedAt;

    public OrderResponseDto(Long id,
                            User user,
                            List<OrderItems> orderItems,
                            LocalDateTime createdAt,
                            String deliveryAddress,
                            String contactPhone,
                            String deliveryMethod,
                            OrderStatus status,
                            LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
        this.deliveryAddress = deliveryAddress;
        this.contactPhone = contactPhone;
        this.deliveryMethod = deliveryMethod;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
