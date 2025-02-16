package org.telran.web.dto;

public class CategoryCreateDto {

    private String categoryTitle;


    public CategoryCreateDto() {
        //
    }

    public CategoryCreateDto(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

}
