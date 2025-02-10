package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Product;
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
                product.getCategoryId(),
                product.getImageUrl()
        );
    }

    @Override
    public Product toEntity(ProductCreateDto productCreateDto) {
        return new Product(
                productCreateDto.getName(),
                productCreateDto.getDescription(),
                productCreateDto.getPrice(),
                categoryService.getByName(productCreateDto.getCategory()),
                productCreateDto.getImage()
        );
    }
}
