package org.telran.web.dto;

public class CategoryResponseDto {

    private Long id;

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
