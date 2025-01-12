package org.telran.web.dto;

import org.telran.web.entity.Storage;

import java.math.BigDecimal;

public class ProductCreateDto {

    private String productTitle;

    private BigDecimal price;

    private String productInfo;

    private Long categoryId;

    private BigDecimal discount;

    private Storage storageList;

    public ProductCreateDto() {
        //
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Storage getStorageList() {
        return storageList;
    }

    public void setStorageList(Storage storageList) {
        this.storageList = storageList;
    }
}
