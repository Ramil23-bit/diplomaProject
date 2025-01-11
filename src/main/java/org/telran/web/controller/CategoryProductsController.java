package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.entity.CategoryProducts;
import org.telran.web.service.CategoryProductsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryProductsController {

    @Autowired
    private CategoryProductsService categoryProductsService;

    @GetMapping
    public List<CategoryProducts> getAll(){
        return categoryProductsService.getAllCategory();
    }

    @GetMapping("/{id}")
    public CategoryProducts getById(@PathVariable(name = "id") Long id){
        return categoryProductsService.getByIdCategory(id);
    }

    @PostMapping
    public CategoryProducts create(@RequestBody @Valid CategoryProducts categoryProducts){
        return categoryProductsService.createCategory(categoryProducts);
    }
}
