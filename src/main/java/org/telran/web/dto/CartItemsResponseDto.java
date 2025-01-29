package org.telran.web.dto;

import org.telran.web.entity.Product;
import org.telran.web.entity.User;

public class CartItemsResponseDto {
    private Long id;

    private Long quantity;

    private Product product;

    private User user;

    public CartItemsResponseDto(Long id, Long quantity, Product product, User user) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
