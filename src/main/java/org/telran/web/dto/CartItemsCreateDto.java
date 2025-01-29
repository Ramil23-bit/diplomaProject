package org.telran.web.dto;

import org.telran.web.entity.Cart;
import org.telran.web.entity.Product;

public class CartItemsCreateDto {

    private Cart cartId;

    private Product productId;

    public CartItemsCreateDto(Cart cartId, Product productId) {
        this.cartId = cartId;
        this.productId = productId;
    }

    public Cart getCartId() {
        return cartId;
    }

    public void setCartId(Cart cartId) {
        this.cartId = cartId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }
}
