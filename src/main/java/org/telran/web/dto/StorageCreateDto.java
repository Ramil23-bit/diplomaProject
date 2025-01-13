package org.telran.web.dto;

import org.telran.web.entity.Product;

import java.util.List;

public class StorageCreateDto {

    private Long amount;

    private List<Product> productList;

    public StorageCreateDto(Long amount, List<Product> productList) {
        this.amount = amount;
        this.productList = productList;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
