package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.CategoryProducts;
import org.telran.web.exception.CategoryProductsNotFoundException;
import org.telran.web.repository.CategoryProductsJpaRepository;

import java.util.List;

@Service
public class CategoryProductsServiceImpl implements CategoryProductsService {

    @Autowired
    private CategoryProductsJpaRepository categoryJpaRepository;
    @Override
    public List<CategoryProducts> getAllCategory() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public CategoryProducts getByIdCategory(Long id) {
        return categoryJpaRepository.findById(id)
                .orElseThrow(() -> new CategoryProductsNotFoundException("Category products by " + id +" not Found"));
    }

    @Override
    public CategoryProducts createCategory(CategoryProducts categoryProducts) {
        return categoryJpaRepository.save(categoryProducts);
    }
}
