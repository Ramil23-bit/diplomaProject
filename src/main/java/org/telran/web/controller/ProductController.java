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
    public ProductResponseDto create(@Valid @RequestBody ProductCreateDto productDto) {
        return createConverter.toDto(productService.create(createConverter.toEntity(productDto)));
    }

    //localhost:8080/api/v1/products?categoryId=0&sort=1&minPrice=0
    //localhost:8080/api/v1/products - без фильтров
    //localhost:8080/api/v1/products?sort=1 -отсортированный список
    //localhost:8080/api/v1/products?minPrice=100 - список с минимальной ценой
    //localhost:8080/api/v1/products?sort=1&minPrice=100 - сортировка по двум параметрам
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
                direction != null ? direction : 0, // Default sorting direction
                minPrice != null ? minPrice : defaultMinPrice, // Default minimum price
                maxPrice != null ? maxPrice : defaultMaxPrice, // Default maximum price
                discount);
        /*
        SELECT * FROM product WHERE categoryId = categoryId AND minPrice > min
         */
        return products.stream()
                .map(createConverter::toDto)
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
