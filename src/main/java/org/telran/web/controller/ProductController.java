package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Product;
import org.telran.web.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for managing products.
 * Provides endpoints for creating, retrieving, updating, and deleting products.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Converter<Product, ProductCreateDto, ProductResponseDto> createConverter;

    /**
     * Creates a new product.
     *
     * @param productDto DTO containing the product details.
     * @return ProductResponseDto representing the created product.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto create(@Valid @RequestBody ProductCreateDto productDto) {
        return createConverter.toDto(productService.create(createConverter.toEntity(productDto)));
    }

    /**
     * Retrieves all products with optional filters.
     *
     * @param categoryId Optional category ID to filter products.
     * @param direction Sorting direction (0 for ascending, 1 for descending).
     * @param minPrice Minimum price filter.
     * @param maxPrice Maximum price filter.
     * @param discount Discount percentage filter.
     * @return List of ProductResponseDto representing the filtered products.
     */
    @GetMapping
    public List<ProductResponseDto> getAll(
            @RequestParam(name = "category_id", required = false) Optional<Long> categoryId,
            @RequestParam(name = "sort", required = false) Integer direction,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "discount", required = false) BigDecimal discount) {
        BigDecimal defaultMinPrice = BigDecimal.ZERO;
        BigDecimal defaultMaxPrice = BigDecimal.valueOf(Long.MAX_VALUE);

        List<Product> products = productService.getAll(
                categoryId.orElse(null),
                direction != null ? direction : 0,
                minPrice != null ? minPrice : defaultMinPrice,
                maxPrice != null ? maxPrice : defaultMaxPrice,
                discount);

        return products.stream()
                .map(createConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id ID of the product.
     * @return ProductResponseDto representing the found product.
     */
    @GetMapping("/{id}")
    public ProductResponseDto getById(@PathVariable Long id) {
        return createConverter.toDto(productService.getById(id));
    }

    /**
     * Updates an existing product.
     *
     * @param id ID of the product to update.
     * @param product DTO containing updated product details.
     * @return ResponseEntity with the updated ProductResponseDto.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @RequestBody @Valid ProductCreateDto product) {
        Product productUpdate = productService.editProducts(id, product);
        ProductResponseDto productResponseDto = createConverter.toDto(productUpdate);
        return ResponseEntity.ok(productResponseDto);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id ID of the product to delete.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        productService.deleteProductsById(id);
        return ResponseEntity.noContent().build();
    }
}
