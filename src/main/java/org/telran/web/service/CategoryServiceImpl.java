package org.telran.web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.exception.CategoryNotFoundException;
import org.telran.web.repository.CategoryJpaRepository;

import java.util.List;

/**
 * Implementation of CategoryService.
 * Handles business logic for managing product categories.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private ProductService productService;

    /**
     * Creates a new category and saves it in the repository.
     *
     * @param category Category entity containing category details.
     * @return The created Category entity.
     * @throws BadArgumentsException if provided data is invalid.
     */
    @Override
    public Category create(Category category) {
        try {
            return categoryJpaRepository.save(category);
        } catch (Exception exception) {
            throw new BadArgumentsException("Entered data is not corrected");
        }
    }

    /**
     * Retrieves all categories from the repository.
     *
     * @return List of all Category entities.
     */
    @Override
    public List<Category> getAll() {
        return categoryJpaRepository.findAll();
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id ID of the category.
     * @return The found Category entity.
     * @throws CategoryNotFoundException if the category is not found.
     */
    @Override
    public Category getById(Long id) {
        return categoryJpaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found"));
    }

    /**
     * Updates the title of a category.
     *
     * @param id ID of the category.
     * @param title New title for the category.
     * @throws CategoryNotFoundException if the category is not found.
     * @throws BadArgumentsException if provided data is invalid.
     */
    @Override
    @Transactional
    public void editTitle(Long id, String title) {
        try {
            if (categoryJpaRepository.updateTitle(id, title) == 0) {
                throw new CategoryNotFoundException("Category with id " + id + " not found");
            }
        } catch (IllegalArgumentException ex) {
            throw new BadArgumentsException("Entered data is not corrected");
        }
    }

    /**
     * Retrieves a category by its name.
     *
     * @param name Name of the category.
     * @return The found Category entity.
     * @throws CategoryNotFoundException if the category is not found.
     */
    @Override
    public Category getByName(String name) {
        return categoryJpaRepository.findByCategoryTitle(name)
                .orElseThrow(() -> new CategoryNotFoundException("Product with name " + name + " not found"));
    }

    /**
     * Adds a product to a category.
     *
     * @param categoryId ID of the category.
     * @param productId ID of the product to add.
     * @return The updated Category entity.
     */
    @Override
    @Transactional
    public Category editListOfProductsAddProduct(Long categoryId, Long productId) {
        Category category = getById(categoryId);
        Product product = productService.getById(productId);
        productService.setCategory(productId, category);
        return category;
    }

    /**
     * Removes a product from a category.
     *
     * @param categoryId ID of the category.
     * @param productId ID of the product to remove.
     * @return The updated Category entity.
     * @throws IllegalArgumentException if the product does not belong to the category.
     */
    @Override
    @Transactional
    public Category editListOfProductsRemoveProduct(Long categoryId, Long productId) {
        Category category = getById(categoryId);
        Product product = productService.getById(productId);
        if (!product.getCategory().equals(category)) {
            throw new IllegalArgumentException("Product does not belong to the category");
        }
        productService.setCategory(productId, null);
        return category;
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id ID of the category to delete.
     * @throws CategoryNotFoundException if the category is not found.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        try {
            categoryJpaRepository.deleteById(id);
        } catch (Exception exception) {
            throw new CategoryNotFoundException("Category by " + id + " not found");
        }
    }
}
