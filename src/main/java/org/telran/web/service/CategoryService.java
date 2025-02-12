package org.telran.web.service;

import org.telran.web.entity.Category;
import org.telran.web.entity.Product;

import java.util.List;

/**
 * Service interface for managing product categories.
 * Provides methods for category creation, retrieval, modification, and deletion.
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param category Category entity containing category details.
     * @return The created Category entity.
     */
    Category create(Category category);

    /**
     * Retrieves all categories.
     *
     * @return List of all Category entities.
     */
    List<Category> getAll();

    /**
     * Retrieves a category by its name.
     *
     * @param name Name of the category.
     * @return The found Category entity.
     */
    Category getByName(String name);

    /**
     * Retrieves a category by its ID.
     *
     * @param id ID of the category.
     * @return The found Category entity.
     */
    Category getById(Long id);

    /**
     * Updates the title of a category.
     *
     * @param id ID of the category.
     * @param title New title for the category.
     */
    void editTitle(Long id, String title);

    /**
     * Adds a product to a category.
     *
     * @param categoryId ID of the category.
     * @param productId ID of the product to add.
     * @return The updated Category entity.
     */
    Category editListOfProductsAddProduct(Long categoryId, Long productId);

    /**
     * Removes a product from a category.
     *
     * @param categoryId ID of the category.
     * @param productId ID of the product to remove.
     * @return The updated Category entity.
     */
    Category editListOfProductsRemoveProduct(Long categoryId, Long productId);

    /**
     * Deletes a category by its ID.
     *
     * @param id ID of the category to delete.
     */
    void delete(Long id);
}