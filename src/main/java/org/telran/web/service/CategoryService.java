package org.telran.web.service;

import org.telran.web.entity.Category;

import java.util.List;

public interface CategoryService {
    
    Category create(Category category);

    List<Category> getAll();

    Category getById(Long id);
}
