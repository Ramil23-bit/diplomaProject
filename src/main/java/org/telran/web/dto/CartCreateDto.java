package org.telran.web.dto;

import org.telran.web.entity.User;

public class CartCreateDto {

    private Long id;

    private User user;

    public CartCreateDto(Long userId, User user) {
        this.user = user;
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
