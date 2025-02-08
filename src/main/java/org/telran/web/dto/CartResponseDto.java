package org.telran.web.dto;

import org.telran.web.entity.User;

public class CartResponseDto {

    private Long id;

    private UserResponseDto user;

    public CartResponseDto() {
    }

    public CartResponseDto(Long id, UserResponseDto user) {
        this.id = id;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }
}
