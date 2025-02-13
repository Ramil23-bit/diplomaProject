package org.telran.web.dto;

import java.math.BigDecimal;

public class ProductResponseDto {

    private Long id;

    private String productTitle;

    private BigDecimal price;

    private BigDecimal discount;

    private Long categoryId;

    private String categoryName;

    private String description;

    private String imageUrl;

    public ProductResponseDto(Long id, String productTitle, BigDecimal price, BigDecimal discount, Long categoryId, String categoryName, String description, String imageUrl) {
        this.id = id;
        this.productTitle = productTitle;
        this.price = price;
        this.discount = discount;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public ProductResponseDto(Long id, String productTitle, BigDecimal price, BigDecimal discount, Long categoryId) {
        this.id = id;
        this.productTitle = productTitle;
        this.price = price;
        this.discount = discount;
        this.categoryId = categoryId;
    }

    public ProductResponseDto(Long id, String productTitle, BigDecimal price, BigDecimal discount, Long categoryId, String imageUrl) {
        this.id = id;
        this.productTitle = productTitle;
        this.price = price;
        this.discount = discount;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId  (Long categoryId) {
        this.categoryId = categoryId;
    }
}
