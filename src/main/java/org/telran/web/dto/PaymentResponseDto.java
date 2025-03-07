package org.telran.web.dto;

import org.telran.web.entity.User;

public class PaymentResponseDto {
    private Long id;

    private User user;

    public PaymentResponseDto(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
