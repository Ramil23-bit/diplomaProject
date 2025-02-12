package org.telran.web.converter;

import org.springframework.stereotype.Component;
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryCreateConverter implements Converter<Category, CategoryCreateDto, CategoryResponseDto> {

    private final ProductService productService;

    public CategoryCreateConverter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getCategoryTitle());
    }

    @Override
    public Category toEntity(CategoryCreateDto categoryCreateDto) {
        List<Long> productIds = categoryCreateDto.getProductIds();

        List<Product> products = (productIds == null || productIds.isEmpty())
                ? List.of()
                : productService.findByIds(productIds);

        return new Category(categoryCreateDto.getCategoryTitle(), products);
    }
}

