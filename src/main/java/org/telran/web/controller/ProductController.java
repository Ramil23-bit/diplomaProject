package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.service.ProductService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Converter<Product, ProductCreateDto, ProductResponseDto> createConverter;

    @PostMapping
    public ProductResponseDto create(@Valid @RequestBody ProductCreateDto productDto) {
        return createConverter.toDto(productService.create(createConverter.toEntity(productDto)));
    }

    @GetMapping
    public List<ProductResponseDto> getAll() {
        return productService.getAll().stream()
                .map(product -> createConverter.toDto(product))
                .collect(Collectors.toList());
    }

    @GetMapping("/discount")
    public List<ProductResponseDto> getAllProductDiscount(@RequestParam String discount) {
        BigDecimal discountValue = discount != null ? new BigDecimal(discount) : null;
        return productService.getAllDiscount(discountValue).stream()
                .map(product -> createConverter.toDto(product))
                .collect(Collectors.toList());
    }

    @GetMapping("/price")
    public List<ProductResponseDto> getAllProductPrice(@RequestParam String minPrice, @RequestParam String maxPrice) {
        BigDecimal priceValueMin = minPrice != null ? new BigDecimal(minPrice) : null;
        BigDecimal priceValueMax = maxPrice != null ? new BigDecimal(maxPrice) : null;
        return productService.getAllProductByPrice(priceValueMin, priceValueMax).stream()
                .map(product -> createConverter.toDto(product))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductResponseDto getById(@PathVariable Long id) {
        return createConverter.toDto(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable(name = "id") Long id, @RequestBody @Valid ProductCreateDto product) {
        Product productUpdate = productService.editProducts(id, product);
        ProductResponseDto productResponseDto = createConverter.toDto(productUpdate);
        return ResponseEntity.ok(productResponseDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        productService.deleteProductsById(id);
    }

    @GetMapping("/sort")
    public List<ProductResponseDto> getProductsSortedByColumnsAscOrDesc(@RequestParam(name = "column") String column, @RequestParam(name = "asc") boolean asc) {
        return productService.getProductsSortedByColumnsAscOrDesc(asc, column).stream()
                .map(product -> createConverter.toDto(product))
                .collect(Collectors.toList());
    }

    @GetMapping("/category")
    public ResponseEntity<List<Product>> getProductByCategory(@RequestParam(name = "categoryTitle") String title){
        List<Product> sortedProducts = productService.getAllProductByCategoryTitle(title);
        return ResponseEntity.ok(sortedProducts);
    }
}
