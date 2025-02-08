package org.telran.web.dto;

import org.telran.web.entity.Storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductCreateDto {

    private String name;
    private BigDecimal price;
    private String description;
    private Long categoryId;
    private String category;
    private Long storageId;
    private BigDecimal discount;
    private Storage storageList;
    private LocalDateTime updateAt;
    private String image;

    public Storage getStorageList() {
        return storageList;
    }

    public void setStorageList(Storage storageList) {
        this.storageList = storageList;
    }

    public ProductCreateDto() {
        //
    }

    public ProductCreateDto(String name, BigDecimal price, String description, Long categoryId,
                            Long storageId, BigDecimal discount, Storage storageList) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.storageId = storageId;
        this.discount = discount;
        this.storageList = storageList;
    }


    public ProductCreateDto(String name, String description, BigDecimal price, String category, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
