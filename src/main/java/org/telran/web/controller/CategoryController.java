package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing product categories.
 * Provides endpoints for creating, retrieving, updating, and deleting categories.
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Converter<Category, CategoryCreateDto, CategoryResponseDto> categoryConverter;

    /**
     * Creates a new category.
     *
     * @param categoryDto DTO containing the category details.
     * @return CategoryResponseDto representing the created category.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@RequestBody CategoryCreateDto categoryDto) {
        return categoryConverter.toDto(categoryService.create(categoryConverter.toEntity(categoryDto)));
    }

    /**
     * Retrieves all categories.
     *
     * @return List of CategoryResponseDto representing all categories.
     */
    @GetMapping
    public List<CategoryResponseDto> getAll() {
        return categoryService.getAll().stream()
                .map(categoryConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id ID of the category.
     * @return CategoryResponseDto representing the found category.
     */
    @GetMapping("/{id}")
    public CategoryResponseDto getById(@PathVariable(name = "id") Long id) {
        return categoryConverter.toDto(categoryService.getById(id));
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id ID of the category to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {
        categoryService.delete(id);
    }

    /**
     * Updates the title of a category.
     *
     * @param id ID of the category.
     * @param newTitle New title for the category.
     * @return Updated CategoryResponseDto.
     */
    @PutMapping("/edit/title/{id}")
    public CategoryResponseDto editTitle(@PathVariable Long id, @RequestParam String newTitle) {
        categoryService.editTitle(id, newTitle);
        return categoryConverter.toDto(categoryService.getById(id));
    }

    /**
     * Adds a product to a category.
     *
     * @param id ID of the category.
     * @param newProduct ID of the product to add.
     * @return Updated CategoryResponseDto.
     */
    @PutMapping("/edit/add_product/{id}")
    public CategoryResponseDto editAddProduct(@PathVariable Long id, @RequestParam Long newProduct) {
        return categoryConverter.toDto(categoryService.editListOfProductsAddProduct(id, newProduct));
    }

    /**
     * Removes a product from a category.
     *
     * @param id ID of the category.
     * @param removeProduct ID of the product to remove.
     * @return Updated CategoryResponseDto.
     */
    @PutMapping("/edit/remove_product/{id}")
    public CategoryResponseDto editRemoveProduct(@PathVariable Long id, @RequestParam Long removeProduct) {
        return categoryConverter.toDto(categoryService.editListOfProductsRemoveProduct(id, removeProduct));
    }
}
