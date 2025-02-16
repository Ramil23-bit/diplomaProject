package org.telran.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StorageCreateDto {

    private Long amount;

    public StorageCreateDto(Long amount) {
        this.amount = amount;
    }

    public StorageCreateDto() {
        //
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}
