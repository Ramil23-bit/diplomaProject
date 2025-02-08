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

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void create() {
        Category newCategory = new Category(null, "New Category", null);
        Category savedCategory = new Category(1L, "New Category", null);

        when(categoryJpaRepository.save(newCategory)).thenReturn(savedCategory);

        Category result = categoryService.create(newCategory);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Category", result.getCategoryTitle());
    }

    @Test
    public void testGetAllCategories() {
        List<Category> categories = Arrays.asList(
                new Category(1L, "Tools and equipment", null),
                new Category(2L, "Planting material", null));
        when(categoryJpaRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAll();
        assertEquals(2, result.size());
        assertEquals("Tools and equipment", result.get(0).getCategoryTitle());
        assertEquals("Planting material", result.get(1).getCategoryTitle());
    }

    @Test
    public void getByIdWhenCategryExists() {
        Long categoryId = 3333333L;
        Category categoryExpected = new Category();
        categoryExpected.setId(categoryId);

        when(categoryJpaRepository.findById(categoryId))
                .thenReturn(Optional.of(categoryExpected));

        Category categoryActual = categoryService.getById(categoryId);

        assertEquals(categoryExpected.getId(), categoryActual.getId());
    }

    @Test
    public void getByIdWhenCategoryNotExists() {
        Long id = 4444444L;
        when(categoryJpaRepository.findById(id))
                .thenThrow(new CategoryNotFoundException("Category not found"));

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getById(id));
    }

    @Test
    void editTitle() {
        Long categoryId = 1L;
        String newTitle = "Updated Title";

        when(categoryJpaRepository.updateTitle(categoryId, newTitle)).thenReturn(1);
        assertDoesNotThrow(() -> categoryService.editTitle(categoryId, newTitle));
        verify(categoryJpaRepository, times(1)).updateTitle(categoryId, newTitle);
    }

    @Test
    void editTitleIfCategoryNotFound() {
        Long categoryId = 1L;
        String newTitle = "Updated Title";

        when(categoryJpaRepository.updateTitle(categoryId, newTitle)).thenReturn(0);
        assertThrows(CategoryNotFoundException.class, () -> categoryService.editTitle(categoryId, newTitle)); // ✅ Исправлено
        verify(categoryJpaRepository, times(1)).updateTitle(categoryId, newTitle);
    }



    @Test
    void editListOfProductsAddProductShouldAddProductToCategory() {
        Long categoryId = 1L;
        Long productId = 100L;

        Category category = new Category(categoryId, "Test Category", null);
        Storage storage = new Storage(1L, 100L); // Убрали `new ArrayList<>()`
        Product product = new Product(productId, "Test Product", BigDecimal.valueOf(100), "Test Description", category, storage, BigDecimal.ZERO, null, null);

        when(productService.getById(productId)).thenReturn(product);
        when(productService.setCategory(productId, category)).thenReturn(product);
        when(categoryJpaRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category updatedCategory = categoryService.editListOfProductsAddProduct(categoryId, productId);

        assertNotNull(updatedCategory);
        assertEquals(categoryId, updatedCategory.getId());

        verify(productService, times(1)).getById(productId);
        verify(productService, times(1)).setCategory(productId, category);
        verify(categoryJpaRepository, times(1)).findById(categoryId);
    }


    @Test
    void editListOfProductsRemoveProductShouldRemoveProductFromCategory() {
        Long categoryId = 1L;
        Long productId = 100L;

        Category category = new Category(categoryId, "Test Category", null);
        Storage storage = new Storage(1L, 100L); // Убрали `new ArrayList<>()`
        Product product = new Product(productId, "Test Product", BigDecimal.valueOf(100), "Test Description", category, storage, BigDecimal.ZERO, null, null);

        when(categoryJpaRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productService.getById(productId)).thenReturn(product);
        when(productService.setCategory(productId, null)).thenReturn(product);

        Category updatedCategory = categoryService.editListOfProductsRemoveProduct(categoryId, productId);

        assertNotNull(updatedCategory);
        assertEquals(categoryId, updatedCategory.getId());

        verify(categoryJpaRepository, times(1)).findById(categoryId);
        verify(productService, times(1)).getById(productId);
        verify(productService, times(1)).setCategory(productId, null);
    }

}
