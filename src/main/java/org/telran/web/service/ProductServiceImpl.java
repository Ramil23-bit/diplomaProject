package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.repository.ProductJpaRepository;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Override
    public List<Product> getAll() {
        return productJpaRepository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return productJpaRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public Product create(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Product setCategory(Long productId, Category category) {
        Product product = getById(productId);
        if(category == null) {
            if(product.getCategory() != null) {
                product.setCategory(null);
                return productJpaRepository.save(product);
            }else{
                return product;
            }
        }
        if(product.getCategory() != null && !product.getCategory().equals(category)) {
            throw new IllegalStateException("Product already belongs to Category");
        }
        product.setCategory(category);
        return productJpaRepository.save(product);
    }
}
