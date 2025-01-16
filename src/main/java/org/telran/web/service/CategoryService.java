package org.telran.web.service;

import org.telran.web.entity.Category;
import org.telran.web.entity.Product;

import java.util.List;

public interface CategoryService {
    
    Category create(Category category);

    List<Category> getAll();

    Category getById(Long id);

    void editTitle(Long id, String title);

    Category editListOfProductsAddProduct(Long categoryId, Long productId);

    Category editListOfProductsRemoveProduct(Long categoryId, Long productId);

    void delete(Long id);

}
