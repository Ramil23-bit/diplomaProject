package org.telran.web.dto;

import org.telran.web.entity.Product;
import org.telran.web.entity.User;

public class FavoritesResponseDto {

    private Long favoriteId;
    private User user;
    private Product product;

    public FavoritesResponseDto() {
        //
    }

    public FavoritesResponseDto(Long favoriteId, User user, Product product) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
