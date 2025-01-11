package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.excdeption.CategoryNotFoundException;
import org.telran.web.repository.CategoryJpaRepository;

@Component
public class ProductCreateConverter implements Converter <Product, ProductCreateDto, ProductResponseDto>{

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Override
    public ProductResponseDto toDto(Product product) {
        Long categoryId;
        if (product.getCategory() == null) {
            throw new CategoryNotFoundException("Category not found for product with ID: " + product.getId());
        } else {
            categoryId = product.getCategory().getId();
        }
        return new ProductResponseDto(
                product.getId(),
                product.getProductTitle(),
                product.getPrice(),
                product.getDiscount(),
                categoryId);
    }

    @Override
    public Product toEntity(ProductCreateDto productCreateDto) {
        Category category = categoryJpaRepository.findById(productCreateDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));;
        category.setId(productCreateDto.getCategoryId());

        return new Product(productCreateDto.getProductTitle(),
                productCreateDto.getPrice(),
                productCreateDto.getProductInfo(),
                productCreateDto.getCategoryId(),
                productCreateDto.getDiscount());
    }
}
