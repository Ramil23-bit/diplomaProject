package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Category;
import org.telran.web.excdeption.CategoryNotFoundException;
import org.telran.web.repository.CategoryJpaRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category create(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public List<Category> getAll() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryJpaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found"));
    }
}
