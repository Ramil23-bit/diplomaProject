package org.telran.web.dto;

import org.telran.web.entity.Cart;
import org.telran.web.entity.Product;

public class CartItemsCreateDto {

    private Long quantity;

    private Long cartId;

    private Long productId;


    public CartItemsCreateDto(Long quantity, Long cartId, Long productId) {
        this.quantity = quantity;
        this.cartId = cartId;
        this.productId = productId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
