package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * Provides endpoints to create, retrieve, update, and delete categories.
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Converter<Category, CategoryCreateDto, CategoryResponseDto> categoryConverter;

    /**
     * Creates a new product category.
     *
     * @param categoryDto The DTO containing category details.
     * @return The created category response DTO.
     */
    @Operation(summary = "Create a new category", description = "Creates a new product category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@RequestBody CategoryCreateDto categoryDto) {
        return categoryConverter.toDto(categoryService.create(categoryConverter.toEntity(categoryDto)));
    }

    /**
     * Retrieves all product categories.
     *
     * @return List of category response DTOs.
     */
    @Operation(summary = "Get all categories", description = "Retrieves a list of all product categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories successfully retrieved")
    })
    @GetMapping
    public List<CategoryResponseDto> getAll() {
        return categoryService.getAll().stream()
                .map(categoryConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category.
     * @return The category response DTO.
     */
    @Operation(summary = "Get category by ID", description = "Retrieves details of a specific category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public CategoryResponseDto getById(@PathVariable(name = "id") Long id) {
        return categoryConverter.toDto(categoryService.getById(id));
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to delete.
     */
    @Operation(summary = "Delete category by ID", description = "Removes a specific category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(name = "id") Long id) {
        categoryService.delete(id);
    }

    /**
     * Updates the title of a category.
     *
     * @param id The ID of the category.
     * @param newTitle The new title of the category.
     * @return The updated category response DTO.
     */
    @Operation(summary = "Edit category title", description = "Updates the title of a category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category title successfully updated"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/edit/title/{id}")
    public CategoryResponseDto editTitle(@PathVariable Long id, @RequestParam String newTitle) {
        categoryService.editTitle(id, newTitle);
        return categoryConverter.toDto(categoryService.getById(id));
    }
}
