package org.telran.web.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

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
        logger.info("Creating new category: {}", category.getCategoryTitle());
        try {
            Category savedCategory = categoryJpaRepository.save(category);
            logger.info("Category created successfully with ID: {}", savedCategory.getId());
            return savedCategory;
        } catch (Exception exception) {
            logger.error("Error creating category: {}", exception.getMessage());
            throw new BadArgumentsException("Entered data is not correct");
        }
    }

    /**
     * Retrieves all categories from the repository.
     *
     * @return List of all Category entities.
     */
    @Override
    public List<Category> getAll() {
        logger.info("Fetching all categories");
        List<Category> categories = categoryJpaRepository.findAll();
        logger.info("Total categories retrieved: {}", categories.size());
        return categories;
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
        logger.info("Fetching category with ID: {}", id);
        return categoryJpaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category with ID {} not found", id);
                    return new CategoryNotFoundException("Category with id " + id + " not found");
                });
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
        logger.info("Fetching category by name: {}", name);
        return categoryJpaRepository.findByCategoryTitle(name)
                .orElseThrow(() -> {
                    logger.error("Category with name {} not found", name);
                    return new CategoryNotFoundException("Category with name " + name + " not found");
                });
    }

    /**
     * Adds a product to a category.
     *
     * @param categoryId ID of the category.
     * @param productId  ID of the product to add.
     * @return The updated Category entity.
     */
    @Override
    @Transactional
    public Category editListOfProductsAddProduct(Long categoryId, Long productId) {
        logger.info("Adding product with ID: {} to category with ID: {}", productId, categoryId);
        Category category = getById(categoryId);
        Product product = productService.getById(productId);
        productService.setCategory(productId, category);
        logger.info("Product with ID: {} successfully added to category with ID: {}", productId, categoryId);
        return category;
    }

    /**
     * Removes a product from a category.
     *
     * @param categoryId ID of the category.
     * @param productId  ID of the product to remove.
     * @return The updated Category entity.
     * @throws IllegalArgumentException if the product does not belong to the category.
     */
    @Override
    @Transactional
    public Category editListOfProductsRemoveProduct(Long categoryId, Long productId) {
        logger.info("Removing product with ID: {} from category with ID: {}", productId, categoryId);
        Category category = getById(categoryId);
        Product product = productService.getById(productId);
        if (!product.getCategory().equals(category)) {
            logger.error("Product with ID: {} does not belong to category with ID: {}", productId, categoryId);
            throw new IllegalArgumentException("Product does not belong to the category");
        }
        productService.setCategory(productId, null);
        logger.info("Product with ID: {} successfully removed from category with ID: {}", productId, categoryId);
        return category;
    }

    /**
     * Updates the title of a category.
     *
     * @param id    ID of the category.
     * @param title New title for the category.
     */
    @Override
    @Transactional
    public void editTitle(Long id, String title) {
        logger.info("Updating category title for ID: {}", id);
        try {
            if (categoryJpaRepository.updateTitle(id, title) == 0) {
                throw new CategoryNotFoundException("Category with id " + id + " not found");
            }
            logger.info("Category title updated successfully for ID: {}", id);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid category title update for ID: {}", id);
            throw new BadArgumentsException("Entered data is not correct");
        }
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id ID of the category to delete.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        logger.info("Deleting category with ID: {}", id);
        try {
            categoryJpaRepository.deleteById(id);
            logger.info("Category with ID {} successfully deleted", id);
        } catch (Exception exception) {
            logger.error("Error deleting category with ID: {}", id);
            throw new CategoryNotFoundException("Category by " + id + " not found");
        }
    }
}
