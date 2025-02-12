package org.telran.web.dto;

import org.telran.web.entity.Product;
import org.telran.web.entity.User;

public class CartItemsResponseDto {
    private Long id;

    private Long quantity;

    private ProductResponseDto product;

    private UserResponseDto user;

    public CartItemsResponseDto(Long id, Long quantity, ProductResponseDto product, UserResponseDto user) {
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

    public ProductResponseDto getProduct() {
        return product;
    }

    public void setProduct(ProductResponseDto product) {
        this.product = product;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }
}
