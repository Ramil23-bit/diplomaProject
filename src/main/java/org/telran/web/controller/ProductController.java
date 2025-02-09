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

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Converter<Product, ProductCreateDto, ProductResponseDto> createConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto create(@Valid @RequestBody ProductCreateDto productDto) {
        return createConverter.toDto(productService.create(createConverter.toEntity(productDto)));
    }

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

    @GetMapping("/{id}")
    public ProductResponseDto getById(@PathVariable Long id) {
        return createConverter.toDto(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @RequestBody @Valid ProductCreateDto product) {
        Product productUpdate = productService.editProducts(id, product);
        ProductResponseDto productResponseDto = createConverter.toDto(productUpdate);
        return ResponseEntity.ok(productResponseDto);
    }

    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        productService.deleteProductsById(id);
        return ResponseEntity.noContent().build();
    }

}
