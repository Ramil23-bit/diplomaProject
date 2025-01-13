package org.telran.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    @JsonBackReference
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

    public Product(Long id, String productTitle, BigDecimal price, String productInfo, Category category, Storage storage, BigDecimal discount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.productTitle = productTitle;
        this.price = price;
        this.productInfo = productInfo;
        this.category = category;
        this.storage = storage;
        this.discount = discount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Product(String productTitle, BigDecimal price, String productInfo, Category category, Storage storage, BigDecimal discount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productTitle = productTitle;
        this.price = price;
        this.productInfo = productInfo;
        this.category = category;
        this.storage = storage;
        this.discount = discount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Product(String productTitle, BigDecimal price, String productInfo, Category category, Storage storage, BigDecimal discount) {
        this.productTitle = productTitle;
        this.price = price;
        this.productInfo = productInfo;
        this.category = category;
        this.storage = storage;
        this.discount = discount;
        this.createdAt = null;
        this.updatedAt = null;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
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

    @JsonProperty("category_id")
    public Long getCategoryId() {
        return category != null ? category.getId() : null;
    }

    @JsonProperty("storage_id")
    public Long getStorageId() {
        return storage != null ? storage.getId() : null;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productTitle='" + productTitle + '\'' +
                ", price=" + price +
                ", productInfo='" + productInfo + '\'' +
                ", category=" + category +
                ", storage=" + storage +
                ", discount=" + discount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
