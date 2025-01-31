package org.telran.web.dto;

import org.telran.web.entity.User;

public class PaymentCreateDto {

    private Long amount;

    private Long userId;

    public PaymentCreateDto(Long amount, Long userId) {
        this.amount = amount;
        this.userId = userId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
