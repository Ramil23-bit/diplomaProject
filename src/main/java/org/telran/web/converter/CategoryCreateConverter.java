package org.telran.web.converter;

import org.springframework.stereotype.Component;
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;

/**
 * Converter class for transforming Category entities to DTOs and vice versa.
 * Handles the conversion between Category, CategoryCreateDto, and CategoryResponseDto.
 */
@Component
public class CategoryCreateConverter implements Converter<Category, CategoryCreateDto, CategoryResponseDto> {

    /**
     * Converts a Category entity to a CategoryResponseDto.
     *
     * @param category The Category entity to convert.
     * @return A CategoryResponseDto representing the category.
     */
    @Override
    public CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getCategoryTitle());
    }

    /**
     * Converts a CategoryCreateDto to a Category entity.
     *
     * @param categoryCreateDto The DTO containing category creation data.
     * @return The created Category entity.
     */
    @Override
    public Category toEntity(CategoryCreateDto categoryCreateDto) {
        return new Category(categoryCreateDto.getCategoryTitle(), categoryCreateDto.getProducts());
    }
}
