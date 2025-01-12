package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Category;
import org.telran.web.exception.CategoryNotFoundException;
import org.telran.web.repository.CategoryJpaRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

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
        categoryExpected.setId(3333333L);

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
}