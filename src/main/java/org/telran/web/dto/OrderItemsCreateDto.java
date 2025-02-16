package org.telran.web.dto;

import java.math.BigDecimal;

public class OrderItemsCreateDto {



    private Long productId;

    private Long quantity;

    private BigDecimal priceAtPurchase;

    public OrderItemsCreateDto() {
        //
    }

    public OrderItemsCreateDto(Long productId, Long quantity, BigDecimal priceAtPurchase) {
        this.productId = productId;
        this.quantity = quantity;
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

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceByPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}


