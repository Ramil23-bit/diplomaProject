package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.entity.Storage;
import org.telran.web.exception.CategoryNotFoundException;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.exception.StorageNotFoundException;
import org.telran.web.repository.CategoryJpaRepository;
import org.telran.web.repository.ProductJpaRepository;
import org.telran.web.repository.StorageJpaRepository;

import java.math.BigDecimal;
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
    public List<Product> getAllDiscount(BigDecimal discount) {
        return productJpaRepository.getAllProductByDiscount(discount);
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
    public Product editProducts(Long id, ProductCreateDto productDto) {
        Product actualProduct = getById(id);
        actualProduct.setPrice(productDto.getPrice());
        actualProduct.setProductInfo(productDto.getProductInfo());
        actualProduct.setDiscount(productDto.getDiscount());
        actualProduct.setUpdatedAt(productDto.getUpdateAt());

        return productJpaRepository.save(actualProduct);

    }

    @Override
    public void deleteProductsById(Long id) {
        productJpaRepository.deleteById(id);
    }



}
