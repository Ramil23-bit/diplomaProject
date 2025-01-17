package org.telran.web.dto;

public class FavoritesCreateDto {

    private Long userId;
    private Long productId;

    public FavoritesCreateDto() {
        //
    }

    public FavoritesCreateDto(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
