package org.telran.web.dto;

import org.telran.web.entity.Product;

public class FavoritesResponseDto {

    private Long favoriteId;
    private UserResponseDto user;
    private Product product;

    public FavoritesResponseDto() {
        //
    }

    public FavoritesResponseDto(Long favoriteId, UserResponseDto user, Product product) {
        this.favoriteId = favoriteId;
        this.user = user;
        this.product = product;
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
