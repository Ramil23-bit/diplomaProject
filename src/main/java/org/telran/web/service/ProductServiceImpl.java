package org.telran.web.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.repository.ProductJpaRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private static final List<String> validColumnName = Arrays.asList("price", "createdAt", "productTitle");

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

        if (direction == 1) {
            cq.orderBy(cb.asc(root.get("price")));
        } else if (direction == -1) {
            cq.orderBy(cb.desc(root.get("price")));
        }

        TypedQuery<Product> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Product> getAllDiscount(BigDecimal discount) {
        return productJpaRepository.getAllProductByDiscount(discount);
    }

    @Override
    public List<Product> getAllProductByPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return productJpaRepository.findAllProductByMinMaxPrice(minPrice, maxPrice);
    }

    @Override
    public List<Product> getAllProductByCategoryTitle(String categoryTitle) {
        return productJpaRepository.findAllProductByCategoryTitle(categoryTitle);
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

    @Override
    public List<Product> getProductsSortedByColumnsAscOrDesc(boolean asc, String column) {
        if(!validColumnName.contains(column)) {
            throw new IllegalArgumentException("Invalid column name: " + column);
        }
        return productJpaRepository.findAll(Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, column));
    }

}
