package org.telran.web.service;

import org.telran.web.dto.ProductCreateDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAll(Long categoryId, int direction, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal discount);

    List<Product> getAllProducts();

    Product getById(Long id);

    Product setCategory(Long productId, Category category);

    Product editProducts(Long id, ProductCreateDto product);

    Optional<Product> getByName(String name);

    void deleteProductsById(Long id);

    Product create(Product product);

}
