package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Category;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.repository.ProductJpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void editProducts(Long id, Product updateProduct) {
        if (updateProduct == null) {
            throw new IllegalArgumentException("Update product cannot be null");
        }

        Product product = getById(id);

        if (updateProduct.getPrice() != null) {
            product.setPrice(updateProduct.getPrice());
        }
        if (updateProduct.getProductInfo() != null) {
            product.setProductInfo(updateProduct.getProductInfo());
        }
        if (updateProduct.getDiscount() != null) {
            product.setDiscount(updateProduct.getDiscount());
            product.setUpdatedAt(updateProduct.getUpdatedAt());
        }

        productJpaRepository.save(product);

    }

    @Override
    public void deleteProductsById(Long id) {
        productJpaRepository.deleteById(id);
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
