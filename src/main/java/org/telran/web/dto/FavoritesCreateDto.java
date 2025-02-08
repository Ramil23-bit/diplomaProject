package org.telran.web.dto;

public class FavoritesCreateDto {

    private Long productId;

    public FavoritesCreateDto() {
        //
    }

    public FavoritesCreateDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
