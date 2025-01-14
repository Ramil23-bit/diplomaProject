package org.telran.web.service;

import org.telran.web.entity.Category;
import org.telran.web.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(Long id);

    Product create(Product product);

    Product setCategory(Long productId, Category category);
}
