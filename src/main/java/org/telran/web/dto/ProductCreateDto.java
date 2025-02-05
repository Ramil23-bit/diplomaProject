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

    public ProductCreateDto(String productTitle, BigDecimal price, String productInfo, Long categoryId,
                            Long storageId, BigDecimal discount, Storage storageList) {
        this.productTitle = productTitle;
        this.price = price;
        this.productInfo = productInfo;
        this.categoryId = categoryId;
        this.storageId = storageId;
        this.discount = discount;
        this.storageList = storageList;
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
        System.out.println("setCategoryId received: " + categoryId + " (type: " + (categoryId != null ? categoryId.getClass().getSimpleName() : "null") + ")");
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
        System.out.println("setStorageId received: " + storageId + " (type: " + (storageId != null ? storageId.getClass().getSimpleName() : "null") + ")");
        this.storageId = storageId;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
