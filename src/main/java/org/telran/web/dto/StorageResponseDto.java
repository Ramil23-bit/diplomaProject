package org.telran.web.dto;

public class StorageResponseDto {

    private Long id;
    private Long amount;

    public StorageResponseDto(Long id, Long amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
