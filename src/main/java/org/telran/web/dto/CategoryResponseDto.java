package org.telran.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("category_title")
    private String categoryTitle;

    public CategoryResponseDto(Long id, String categoryTitle) {
        this.id = id;
        this.categoryTitle = categoryTitle;
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
}
