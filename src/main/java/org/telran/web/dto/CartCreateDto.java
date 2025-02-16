package org.telran.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartCreateDto {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("quantity")
    private Long quantity;

    public CartCreateDto(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartCreateDto() {
        //
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
