package org.telran.web.dto;

import java.math.BigDecimal;

public class OrderItemsCreateDto {

    private Long quantity;

    private Long productId;

    private BigDecimal priceByPurchase;

    public OrderItemsCreateDto(Long quantity, Long productId, BigDecimal priceByPurchase) {
        this.quantity = quantity;
        this.productId = productId;
        this.priceByPurchase = priceByPurchase;
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

    public BigDecimal getPriceByPurchase() {
        return priceByPurchase;
    }

    public void setPriceByPurchase(BigDecimal priceByPurchase) {
        this.priceByPurchase = priceByPurchase;
    }
}
