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

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Converter<Category, CategoryCreateDto, CategoryResponseDto> categoryConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@RequestBody CategoryCreateDto categoryDto) {
        Category category = categoryConverter.toEntity(categoryDto);
        return categoryConverter.toDto(categoryService.create(category));
    }


    @GetMapping
    public List<CategoryResponseDto> getAll() {
        return categoryService.getAll().stream()
                .map(categoryConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getById(@PathVariable Long id) {
        return categoryConverter.toDto(categoryService.getById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @PutMapping("/edit/title/{id}")
    public CategoryResponseDto editTitle(@PathVariable Long id, @RequestParam String newTitle) {
        categoryService.editTitle(id, newTitle);
        return categoryConverter.toDto(categoryService.getById(id));
    }

    @PutMapping("/edit/remove_product/{id}")
    public CategoryResponseDto editRemoveProduct(@PathVariable Long id, @RequestParam Long removeProduct) {
        return categoryConverter.toDto(categoryService.editListOfProductsRemoveProduct(id, removeProduct));
    }
}
