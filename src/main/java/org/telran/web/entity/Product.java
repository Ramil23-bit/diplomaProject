package org.telran.web.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "product_info")
    private String productInfo;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Product() {
        //
    }

    public Product(Long id, String productTitle, BigDecimal price, String productInfo, Category category, BigDecimal discount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.productTitle = productTitle;
        this.price = price;
        this.productInfo = productInfo;
        this.category = category;
        this.discount = discount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Product(String productTitle, BigDecimal price, String productInfo, Category category, BigDecimal discount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productTitle = productTitle;
        this.price = price;
        this.productInfo = productInfo;
        this.category = category;
        this.discount = discount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Product(Long id, String productTitle) {
        this.id = id;
        this.productTitle = productTitle;
    }


    public Product(String productTitle, BigDecimal price, String productInfo, Long categoryId, BigDecimal discount) {
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

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productTitle='" + productTitle + '\'' +
                ", price=" + price +
                ", productInfo='" + productInfo + '\'' +
                ", category=" + category +
                ", discount=" + discount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
