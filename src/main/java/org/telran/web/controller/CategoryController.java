package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.entity.Category;
import org.telran.web.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @GetMapping
    public List<Category>getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public Category getById(Long id) {
        return categoryService.getById(id);
    }
}
