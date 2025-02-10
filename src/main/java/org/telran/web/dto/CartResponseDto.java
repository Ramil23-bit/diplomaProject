package org.telran.web.dto;

import org.telran.web.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CartResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user")
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
