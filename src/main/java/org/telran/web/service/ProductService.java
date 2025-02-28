package org.telran.web.service;

import org.telran.web.dto.ProductCreateDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing products.
 * Provides methods for product retrieval, modification, and deletion.
 */
public interface ProductService {

    /**
     * Retrieves all products with optional filters.
     *
     * @param categoryId ID of the category to filter products.
     * @param direction  Sorting direction (ascending or descending).
     * @param minPrice   Minimum price filter.
     * @param maxPrice   Maximum price filter.
     * @param discount   Discount percentage filter.
     * @return List of filtered Product entities.
     */
    List<Product> getAll(Long categoryId, int direction, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal discount, String sortBy);

    /**
     * Retrieves all products without filters.
     *
     * @return List of all Product entities.
     */
    List<Product> getAllProducts();

    /**
     * Retrieves a product by its ID.
     *
     * @param id ID of the product.
     * @return The found Product entity.
     */
    Product getById(Long id);

    /**
     * Assigns a category to a product.
     *
     * @param productId ID of the product.
     * @param category  Category entity to assign.
     * @return The updated Product entity.
     */
    Product setCategory(Long productId, Category category);

    /**
     * Updates an existing product.
     *
     * @param id      ID of the product to update.
     * @param product DTO containing updated product details.
     * @return The updated Product entity.
     */
    Product editProducts(Long id, ProductCreateDto product);

    /**
     * Retrieves a product by its name.
     *
     * @param name Name of the product.
     * @return An Optional containing the found Product entity, or empty if not found.
     */
    Optional<Product> getByName(String name);

    /**
     * Deletes a product by its ID.
     *
     * @param id ID of the product to delete.
     */
    void deleteProductsById(Long id);

    /**
     * Creates a new product.
     *
     * @param product Product entity containing product details.
     * @return The created Product entity.
     */
    Product create(Product product);

    Product createProductDays();
}
