package org.telran.web.dto;

import org.telran.web.entity.User;

public class CartResponseDto {

    private Long id;

    public CartResponseDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
