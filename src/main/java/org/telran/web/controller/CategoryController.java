package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.service.CategoryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for managing product categories.
 * Provides endpoints to create, retrieve, update, and delete categories.
 */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Converter<Category, CategoryCreateDto, CategoryResponseDto> categoryConverter;

    /**
     * Creates a new category.
     *
     * @param categoryDto The DTO containing category details.
     * @return The created category response DTO.
     */
    @Operation(summary = "Create a new category", description = "Registers a new product category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@RequestBody CategoryCreateDto categoryDto) {
        logger.info("Received request to create category: {}", categoryDto);
        CategoryResponseDto response = categoryConverter.toDto(categoryService.create(categoryConverter.toEntity(categoryDto)));
        logger.info("Category created successfully with ID: {}", response.getId());
        return response;
    }

    /**
     * Retrieves all categories.
     *
     * @return List of category response DTOs.
     */
    @Operation(summary = "Get all categories", description = "Retrieves a list of all product categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories successfully retrieved")
    })
    @GetMapping
    public List<CategoryResponseDto> getAll() {
        logger.info("Fetching all categories");
        List<CategoryResponseDto> categories = Optional.ofNullable(categoryService.getAll())
                .orElse(Collections.emptyList())
                .stream()
                .map(categoryConverter::toDto)
                .collect(Collectors.toList());
        logger.info("Total categories retrieved: {}", categories.size());
        return categories;
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
        logger.info("Fetching category with ID: {}", id);
        CategoryResponseDto category = categoryConverter.toDto(categoryService.getById(id));
        logger.info("Category retrieved: {}", category);
        return category;
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
    public void deleteById(@PathVariable(name = "id") Long id) {
        logger.info("Request to delete category with ID: {}", id);
        categoryService.delete(id);
        logger.info("Category with ID {} successfully deleted", id);
    }
}
