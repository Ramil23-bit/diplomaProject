package org.telran.web.dto;

import java.util.List;

public class CategoryCreateDto {
    private String categoryTitle;
    private List<Long> productIds; // Список ID продуктов

    public CategoryCreateDto(String categoryTitle, List<Long> productIds) { // Принимаем список Long ID
        this.categoryTitle = categoryTitle;
        this.productIds = productIds; // Теперь правильно сохраняем productIds
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public List<Long> getProductIds() { // Исправлено название метода (camelCase)
        return productIds;
    }

    public void setProductIds(List<Long> productIds) { // Исправлено название метода (camelCase)
        this.productIds = productIds; // Теперь правильно обновляется productIds
    }
}
