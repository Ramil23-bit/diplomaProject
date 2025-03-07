package org.telran.web.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_title")
    //@NotNull
    private String categoryTitle;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();

    public Category(Long id, String categoryTitle, List<Product> products) {
        this.id = id;
        this.categoryTitle = categoryTitle;
        this.products = products != null ? products : new ArrayList<>();
    }

    public Category(Long id, String categoryTitle) {
        this.id = id;
        this.categoryTitle = categoryTitle;
        this.products = new ArrayList<>();
    }

    public Category(String categoryTitle, List<Product> products) {
        this.categoryTitle = categoryTitle;
        this.products = products != null ? products : new ArrayList<>();
    }

    public Category(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public Category() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products != null ? products : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryTitle='" + categoryTitle + '\'' +
                ", productsCount=" + (products != null ? products.size() : 0) +
                '}';
    }
}

