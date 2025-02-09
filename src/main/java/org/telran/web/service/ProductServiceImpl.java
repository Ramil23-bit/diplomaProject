package org.telran.web.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.entity.Storage;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.repository.ProductJpaRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Lazy
    @Autowired
    private CategoryService categoryService;

    private static final List<String> validColumnName = Arrays.asList("price", "createdAt", "productTitle");

    @Override
    public List<Product> getAll(Long categoryId, int direction, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal discount) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        Predicate predicate = cb.conjunction();
        if (categoryId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
        }
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) > 0) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        if (discount != null) {
            predicate = cb.and(predicate, cb.equal(root.get("discount"), discount));
        }

        cq.where(predicate);

        try {
            root.get("createdAt");
            cq.orderBy(cb.asc(root.get("createdAt")));
        } catch (IllegalArgumentException e) {
            cq.orderBy(cb.asc(root.get("price"))); // Безопасная альтернатива
        }

        TypedQuery<Product> query = entityManager.createQuery(cq);
        return query.getResultList();
    }


    @Override
    public List<Product> getAllProducts() {
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
    public Product editProducts(Long id, ProductCreateDto productDto) {
        Product actualProduct = getById(id);
        actualProduct.setProductTitle(productDto.getName());
        actualProduct.setProductInfo(productDto.getDescription());
        actualProduct.setPrice(productDto.getPrice());
        actualProduct.setCategory(categoryService.getByName(productDto.getCategory()));
        actualProduct.setDiscount(productDto.getDiscount());
        actualProduct.setUpdatedAt(productDto.getUpdateAt());
        actualProduct.setUpdatedAt(productDto.getUpdateAt());

        try {
            return productJpaRepository.save(actualProduct);
        } catch (Exception e) {
            throw new BadArgumentsException("Form is not completed correctly");
        }
    }

    @Override
    public Optional<Product> getByName(String name) {
        return productJpaRepository.findByName(name);
    }

    @Override
    public void deleteProductsById(Long id) {
        Product product = getById(id);
        productJpaRepository.deleteById(product.getId());
    }
}
