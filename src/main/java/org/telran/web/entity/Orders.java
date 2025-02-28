package org.telran.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.telran.web.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> orderItems = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    //@NotNull
    private String deliveryAddress;
    private String contactPhone;

    //@NotNull
    private String deliveryMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Orders() {
        //
    }

    public Orders(List<OrderItems> orderItems, String deliveryAddress, String deliveryMethod) {
        this.orderItems = orderItems;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
    }

    public Orders(String deliveryAddress, String deliveryMethod) {
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
    }

    public Orders(User user, String deliveryAddress, String deliveryMethod) {
        this.user = user;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
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

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }
}
