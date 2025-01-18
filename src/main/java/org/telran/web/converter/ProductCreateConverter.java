package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.entity.Storage;
import org.telran.web.exception.CategoryNotFoundException;
import org.telran.web.repository.CategoryJpaRepository;
import org.telran.web.service.CategoryService;
import org.telran.web.service.StorageService;

@Component
public class ProductCreateConverter implements Converter<Product, ProductCreateDto, ProductResponseDto> {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    @Override
    public ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getProductTitle(),
                product.getPrice(),
                product.getDiscount(),
                product.getCategoryId()
        );
    }

    @Override
    public Product toEntity(ProductCreateDto productCreateDto) {
        return new Product(
                productCreateDto.getProductTitle(),
                productCreateDto.getPrice(),
                productCreateDto.getProductInfo(),
                categoryService.getById(productCreateDto.getCategoryId()),
                storageService.getByIdStorage(productCreateDto.getStorageId()),
                productCreateDto.getDiscount()
        );
    }
}
