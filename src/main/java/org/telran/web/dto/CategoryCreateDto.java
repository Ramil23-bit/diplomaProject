package org.telran.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.telran.web.entity.Product;

import java.util.List;

public class CategoryCreateDto {

    private String categoryTitle;

    @JsonDeserialize(contentAs = Product.class)
    private List<Product> products;

    public CategoryCreateDto(String categoryTitle, List<Product> products) {
        this.categoryTitle = categoryTitle;
        this.products = products;
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
        this.products = products;
    }
}
