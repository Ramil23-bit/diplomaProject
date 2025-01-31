package org.telran.web.dto;

import org.telran.web.entity.Orders;
import org.telran.web.entity.Product;

import java.math.BigDecimal;

public class OrderItemsResponseDto {

    private Long id;

    private Long quantity;

    private BigDecimal priceAtPurchase;

    private Product product;

    private Orders orders;

    public OrderItemsResponseDto(Long id, Long quantity, BigDecimal priceAtPurchase, Product product, Orders orders) {
        this.id = id;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
        this.product = product;
        this.orders = orders;
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

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
