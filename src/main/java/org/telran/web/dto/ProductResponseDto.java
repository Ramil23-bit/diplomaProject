package org.telran.web.dto;

import java.math.BigDecimal;

public class ProductResponseDto {

    private Long id;

    private String productTitle;

    private BigDecimal price;

    private BigDecimal discount;

    private Long categoryId;

    public ProductResponseDto(Long id, String productTitle, BigDecimal price, BigDecimal discount, Long categoryId) {
        this.id = id;
        this.productTitle = productTitle;
        this.price = price;
        this.discount = discount;
        this.categoryId = categoryId;
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
