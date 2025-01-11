package org.telran.web.service;

import org.telran.web.entity.CategoryProducts;

import java.util.List;

public interface CategoryProductsService {
    List<CategoryProducts> getAllCategory();

    CategoryProducts getByIdCategory(Long id);

    CategoryProducts createCategory(CategoryProducts categoryProducts);
}
