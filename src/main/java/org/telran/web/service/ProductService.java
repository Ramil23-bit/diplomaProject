package org.telran.web.service;

import org.telran.web.dto.ProductCreateDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<Product> getAll();

    List<Product> getAllDiscount(BigDecimal discount);

    List<Product> getAllProductByPrice(BigDecimal price);

    Product getById(Long id);

    Product setCategory(Long productId, Category category);

    Product editProducts(Long id, ProductCreateDto product);

    void deleteProductsById(Long id);

    Product create(Product product);

    List<Product> getProductsSortedByColumnsAscOrDesc(boolean asc, String column);
}
