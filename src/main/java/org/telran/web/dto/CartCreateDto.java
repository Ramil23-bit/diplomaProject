package org.telran.web.dto;

import org.telran.web.entity.User;

public class CartCreateDto {

    private Long id;

    private Long userId;

    public CartCreateDto(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getUser() {
        return userId;
    }

    public void setUser(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
