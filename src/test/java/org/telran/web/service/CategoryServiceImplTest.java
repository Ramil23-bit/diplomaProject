package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.exception.CategoryNotFoundException;
import org.telran.web.repository.CategoryJpaRepository;
import org.telran.web.entity.Storage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *   Key Features:
 * - Uses `@ExtendWith(MockitoExtension.class)` for Mockito integration.
 * - Mocks `CategoryJpaRepository` and `ProductServiceImpl` to isolate service logic.
 * - Covers **category creation, retrieval, updating, and product management**.
 * - Ensures **exception handling for missing categories**.
 */

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    /**
     **Test Case:** Successfully create a new category.
     **Expected Result:** Returns the created category.
     */
    @Test
    void create() {
        Category newCategory = new Category(null, "New Category", null);
        Category savedCategory = new Category(1L, "New Category", null);

        when(categoryJpaRepository.save(newCategory)).thenReturn(savedCategory);

        Category result = categoryService.create(newCategory);

        assertNotNull(result);  // Category should not be null
        assertEquals(1L, result.getId());  // ID must match expected value
        assertEquals("New Category", result.getCategoryTitle());  // Title should match
    }

    /**
     **Test Case:** Retrieve all categories.
     **Expected Result:** Returns a list of categories.
     */
    @Test
    public void testGetAllCategories() {
        List<Category> categories = Arrays.asList(
                new Category(1L, "Tools and equipment", null),
                new Category(2L, "Planting material", null));
        when(categoryJpaRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAll();
        assertEquals(2, result.size());  // The size must match expected value
        assertEquals("Tools and equipment", result.get(0).getCategoryTitle());  // Verify category title
        assertEquals("Planting material", result.get(1).getCategoryTitle());  // Verify category title
    }

    /**
     **Test Case:** Retrieve category by ID when it exists.
     **Expected Result:** Returns the expected category.
     */
    @Test
    public void getByIdWhenCategoryExists() {
        Long categoryId = 3333333L;
        Category categoryExpected = new Category();
        categoryExpected.setId(categoryId);

        when(categoryJpaRepository.findById(categoryId))
                .thenReturn(Optional.of(categoryExpected));

        Category categoryActual = categoryService.getById(categoryId);

        assertEquals(categoryExpected.getId(), categoryActual.getId());  // Verify retrieved ID
    }

    /**
     **Test Case:** Retrieve category by ID when it does not exist.
     **Expected Result:** Throws `CategoryNotFoundException`.
     */
    @Test
    public void getByIdWhenCategoryNotExists() {
        Long id = 4444444L;
        when(categoryJpaRepository.findById(id))
                .thenThrow(new CategoryNotFoundException("Category not found"));

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getById(id));  // Expect exception
    }

    /**
     **Test Case:** Successfully update category title.
     **Expected Result:** The category title is updated.
     */
    @Test
    void editTitle() {
        Long categoryId = 1L;
        String newTitle = "Updated Title";

        when(categoryJpaRepository.updateTitle(categoryId, newTitle)).thenReturn(1);
        assertDoesNotThrow(() -> categoryService.editTitle(categoryId, newTitle));
        verify(categoryJpaRepository, times(1)).updateTitle(categoryId, newTitle);  // Ensure update method is called
    }

    /**
     **Test Case:** Update category title when category does not exist.
     **Expected Result:** Throws `CategoryNotFoundException`.
     */
    @Test
    void editTitleIfCategoryNotFound() {
        Long categoryId = 1L;
        String newTitle = "Updated Title";

        when(categoryJpaRepository.updateTitle(categoryId, newTitle)).thenReturn(0);
        assertThrows(CategoryNotFoundException.class, () -> categoryService.editTitle(categoryId, newTitle));
        verify(categoryJpaRepository, times(1)).updateTitle(categoryId, newTitle);  // Ensure update method is called
    }

    /**
     **Test Case:** Add a product to a category.
     **Expected Result:** The product is associated with the category.
     */
    @Test
    void editListOfProductsAddProductShouldAddProductToCategory() {
        Long categoryId = 1L;
        Long productId = 100L;

        Category category = new Category(categoryId, "Test Category", null);
        Storage storage = new Storage(1L, 100L);
        Product product = new Product(productId, "Test Product", BigDecimal.valueOf(100), "Test Description", category, storage, BigDecimal.ZERO, null, null);

        when(productService.getById(productId)).thenReturn(product);
        when(productService.setCategory(productId, category)).thenReturn(product);
        when(categoryJpaRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category updatedCategory = categoryService.editListOfProductsAddProduct(categoryId, productId);

        assertNotNull(updatedCategory);  // The category should not be null
        assertEquals(categoryId, updatedCategory.getId());  // The category ID should match

        verify(productService, times(1)).getById(productId);
        verify(productService, times(1)).setCategory(productId, category);
        verify(categoryJpaRepository, times(1)).findById(categoryId);
    }

    /**
     **Test Case:** Remove a product from a category.
     **Expected Result:** The product is removed from the category.
     */
    @Test
    void editListOfProductsRemoveProductShouldRemoveProductFromCategory() {
        Long categoryId = 1L;
        Long productId = 100L;

        Category category = new Category(categoryId, "Test Category", null);
        Storage storage = new Storage(1L, 100L);
        Product product = new Product(productId, "Test Product", BigDecimal.valueOf(100), "Test Description", category, storage, BigDecimal.ZERO, null, null);

        when(categoryJpaRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productService.getById(productId)).thenReturn(product);
        when(productService.setCategory(productId, null)).thenReturn(product);

        Category updatedCategory = categoryService.editListOfProductsRemoveProduct(categoryId, productId);

        assertNotNull(updatedCategory);  // The category should not be null
        assertEquals(categoryId, updatedCategory.getId());  // The category ID should match

        verify(categoryJpaRepository, times(1)).findById(categoryId);
        verify(productService, times(1)).getById(productId);
        verify(productService, times(1)).setCategory(productId, null);
    }
}
