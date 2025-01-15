package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.telran.web.converter.Converter;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Converter<Category, CategoryCreateDto, CategoryResponseDto> categoryConverter;

    @PostMapping
    public CategoryResponseDto create(@RequestBody CategoryCreateDto categoryDto) {
        return categoryConverter.toDto(categoryService.create(categoryConverter.toEntity(categoryDto)));
    }

    @GetMapping
    public List<Category>getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable(name = "id") Long id) {
        return categoryService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {categoryService.delete(id);}

    @PutMapping("/edit/title/{id}")
    public CategoryResponseDto editTitle(@PathVariable Long id, @RequestParam String newTitle) {
        categoryService.editTitle(id, newTitle);
        return categoryConverter.toDto(categoryService.getById(id));
    }

    @PutMapping("/edit/add_product/{id}")
    public CategoryResponseDto editAddProduct(@PathVariable Long id, @RequestParam Long newProduct) {
        return categoryConverter.toDto(categoryService.editListOfProductsAddProduct(id, newProduct));
    }

    @PutMapping("/edit/remove_product/{id}")
    public CategoryResponseDto editRemoveProduct(@PathVariable Long id, @RequestParam Long removeProduct) {
        return categoryConverter.toDto(categoryService.editListOfProductsRemoveProduct(id, removeProduct));
    }
}
