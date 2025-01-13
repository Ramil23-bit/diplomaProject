package org.telran.web.converter;

import org.springframework.stereotype.Component;
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;
@Component
public class CategoryCreateConverter implements Converter<Category, CategoryCreateDto, CategoryResponseDto>{
    @Override
    public CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getCategoryTitle());
    }

    @Override
    public Category toEntity(CategoryCreateDto categoryCreateDto) {
        return new Category(categoryCreateDto.getCategoryTitle(), categoryCreateDto.getProducts());
    }
}
