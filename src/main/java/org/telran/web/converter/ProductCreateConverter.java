package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Product;
import org.telran.web.service.CategoryService;
import org.telran.web.service.StorageService;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Converter class for transforming Product entities to DTOs and vice versa.
 * Handles the conversion between Product, ProductCreateDto, and ProductResponseDto.
 */
@Component
public class ProductCreateConverter implements Converter<Product, ProductCreateDto, ProductResponseDto> {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    /**
     * Converts a Product entity to a ProductResponseDto.
     *
     * @param product The Product entity to convert.
     * @return A ProductResponseDto representing the product.
     */
    @Override
    public ProductResponseDto toDto(Product product) {
        if(null == product.getDiscount()) {
            product.setDiscount(BigDecimal.ZERO);
        }
        if (product.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountMultiplier = BigDecimal.ONE.subtract(product.getDiscount().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            BigDecimal discountedPrice = product.getPrice().multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
            product.setPrice(discountedPrice);
        }
        return new ProductResponseDto(
                product.getId(),
                product.getProductTitle(),
                product.getPrice(),
                product.getDiscount(),
                product.getCategoryId(),
                product.getCategory() != null ? product.getCategory().getCategoryTitle() : "No Category",
                product.getProductInfo() != null ? product.getProductInfo() : "No Description",
                product.getImageUrl() != null ? product.getImageUrl() : ""
        );
    }

    /**
     * Converts a ProductCreateDto to a Product entity.
     *
     * @param productCreateDto The DTO containing product creation data.
     * @return The created Product entity.
     */
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