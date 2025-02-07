package org.telran.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "product_title")
    private String productTitle;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "product_info")
    private String productInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "storage_id")
    @JsonIgnore
    private Storage storage;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany
    //@JoinColumn(name = "product_id")
    private List<Favorites> favorites = new ArrayList<>();

    public Product() {
        //
    }

    public Product(Long id, String productTitle, BigDecimal price, String productInfo, Category category, Storage storage, BigDecimal discount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (category == null || storage == null) {
            throw new IllegalArgumentException("Category and Storage cannot be null");
        }
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

    public Product(Long id, String productTitle, BigDecimal price, String productInfo, Category category, Storage storage, BigDecimal discount) {
        this.id = id;
        this.productTitle = productTitle;
        this.price = price;
        this.productInfo = productInfo;
        this.category = category;
        this.storage = storage;
        this.discount = discount;
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
        if (category == null || storage == null) {
            throw new IllegalArgumentException("Category and Storage cannot be null");
        }
        this.productTitle = productTitle;
        this.price = price;
        this.productInfo = productInfo;
        this.category = category;
        this.storage = storage;
        this.discount = discount;
    }

    public Product(Long id, String productTitle, BigDecimal price,
                   String productInfo, BigDecimal discount) {
        this.id = id;
        this.productTitle = productTitle;
        this.price = price;
        this.productInfo = productInfo;
        this.discount = discount;
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
                ", categoryId=" + (category != null ? category.getId() : "null") +
                ", storageId=" + (storage != null ? storage.getId() : "null") +
                ", discount=" + discount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", favoritesCount=" + favorites.size() +
                '}';
    }
}

