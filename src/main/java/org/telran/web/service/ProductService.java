package org.telran.web.service;

import org.springframework.data.domain.Sort;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<Product> getAll(Long categoryId, int direction, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal discount);

    List<Product> getAllDiscount(BigDecimal discount);

    List<Product> getAllProductByPrice(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> getAllProductByCategoryTitle(String categoryTitle);

    Product getById(Long id);

    Product setCategory(Long productId, Category category);

    Product editProducts(Long id, ProductCreateDto product);

    void deleteProductsById(Long id);

    Product create(Product product);

    List<Product> getProductsSortedByColumnsAscOrDesc(boolean asc, String column);

}
