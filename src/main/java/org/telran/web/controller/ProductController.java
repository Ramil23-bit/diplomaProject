package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Provides endpoints to create, retrieve, update, and delete products.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private Converter<Product, ProductCreateDto, ProductResponseDto> createConverter;

    /**
     * Creates a new product.
     *
     * @param productDto The DTO containing product details.
     * @return The created product response DTO.
     */
    @Operation(summary = "Create a new product", description = "Registers a new product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto create(@RequestBody ProductCreateDto productDto) {
        logger.info("Received request to create product: {}", productDto);
        ProductResponseDto response = createConverter.toDto(productService.create(createConverter.toEntity(productDto)));
        logger.info("Product created successfully with ID: {}", response.getId());
        return response;
    }

    /**
     * Retrieves all products with optional filtering and sorting.
     *
     * @return List of product response DTOs.
     */
    @Operation(summary = "Get all products", description = "Retrieves a list of all products, with optional filters and sorting.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products successfully retrieved")
    })
    @GetMapping
    public List<ProductResponseDto> getAll(
            @RequestParam(name = "category_id", required = false) Optional<Long> categoryId,
            @RequestParam(name = "sort", required = false) Integer direction,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "discount", required = false) BigDecimal discount) {
        logger.info("Fetching all products with filters - category: {}, minPrice: {}, maxPrice: {}, discount: {}",
                categoryId.orElse(null), minPrice, maxPrice, discount);
        List<Product> products = productService.getAll(
                categoryId.orElse(null),
                direction != null ? direction : 0,
                minPrice != null ? minPrice : BigDecimal.ZERO,
                maxPrice != null ? maxPrice : BigDecimal.valueOf(Long.MAX_VALUE),
                discount);
        logger.info("Total products retrieved: {}", products.size());
        return products.stream()
                .map(createConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product.
     * @return The product response DTO.
     */
    @Operation(summary = "Get product by ID", description = "Retrieves details of a specific product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ProductResponseDto getById(@PathVariable Long id) {
        logger.info("Fetching product with ID: {}", id);
        ProductResponseDto product = createConverter.toDto(productService.getById(id));
        logger.info("Product retrieved: {}", product);
        return product;
    }

    /**
     * Updates an existing product.
     *
     * @param id The ID of the product.
     * @param product The updated product details.
     * @return The updated product response DTO.
     */
    @Operation(summary = "Update product", description = "Updates an existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully updated"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @RequestBody ProductCreateDto product) {
        logger.info("Received request to update product with ID: {}", id);
        Product productUpdate = productService.editProducts(id, product);
        ProductResponseDto productResponseDto = createConverter.toDto(productUpdate);
        logger.info("Product updated successfully: {}", productResponseDto);
        return ResponseEntity.ok(productResponseDto);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     */
    @Operation(summary = "Delete product by ID", description = "Removes a specific product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        logger.info("Request to delete product with ID: {}", id);
        productService.deleteProductsById(id);
        logger.info("Product with ID {} successfully deleted", id);
        return ResponseEntity.noContent().build();
    }
}
