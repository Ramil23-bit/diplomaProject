package org.telran.web.dto;

import org.telran.web.entity.Storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductCreateDto {

    private String productTitle;
    private BigDecimal price;
    private String productInfo;
    private Long categoryId;
    private Long storageId;
    private BigDecimal discount;
    private Storage storageList;

    private LocalDateTime updateAt;

    public Storage getStorageList() {
        return storageList;
    }

    public void setStorageList(Storage storageList) {
        this.storageList = storageList;
    }

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

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
