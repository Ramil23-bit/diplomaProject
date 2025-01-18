package org.telran.web.service;

import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.entity.Storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(Long id);

    Product setCategory(Long productId, Category category);

    void editProducts(Long id, Product product);

    void deleteProductsById(Long id);

    Product create(Product product);
}
