package org.telran.web.dto;

import java.math.BigDecimal;

public class OrderItemsCreateDto {

    private Long quantity;

    private Long productId;

    private Long orderId;

    private BigDecimal priceAtPurchase;

    public OrderItemsCreateDto(Long quantity, Long productId, Long orderId, BigDecimal priceAtPurchase) {
        this.quantity = quantity;
        this.productId = productId;
        this.orderId = orderId;
        this.priceAtPurchase = priceAtPurchase;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceByPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}


