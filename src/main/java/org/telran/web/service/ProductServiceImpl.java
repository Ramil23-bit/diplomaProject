package org.telran.web.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.repository.ProductJpaRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ProductService.
 * Handles business logic for managing products, including retrieval, filtering, updating, and deletion.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductJpaRepository productJpaRepository;
    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private CategoryService categoryService;

    private static final List<String> validColumnName = Arrays.asList("price", "createdAt", "productTitle");

    /**
     * Retrieves all products with optional filters for category, price range, and discount.
     *
     * @param categoryId ID of the category to filter products.
     * @param direction Sorting direction (ascending or descending).
     * @param minPrice Minimum price filter.
     * @param maxPrice Maximum price filter.
     * @param discount Discount percentage filter.
     * @return List of filtered Product entities.
     */
    @Override
    public List<Product> getAll(Long categoryId, int direction, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal discount, String sortBy) {
        logger.info("Fetching all products with filters - categoryId: {}, minPrice: {}, maxPrice: {}, discount: {}, sortBy: {}, direction: {}",
                categoryId, minPrice, maxPrice, discount, sortBy, direction);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (categoryId != null) {
            predicates.add(cb.equal(root.get("category").get("id"), categoryId));
        }
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) > 0) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
            predicates.add(cb.greaterThan(root.get("discount"), BigDecimal.ZERO));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        if (sortBy != null) {
            switch (sortBy) {
                case "price":
                    cq.orderBy(direction == 1 ? cb.asc(root.get("price")) : cb.desc(root.get("price")));
                    break;
                case "createdAt":
                    cq.orderBy(direction == 1 ? cb.asc(root.get("createdAt")) : cb.desc(root.get("createdAt")));
                    break;
                case "productTitle":
                    cq.orderBy(direction == 1 ? cb.asc(root.get("productTitle")) : cb.desc(root.get("productTitle")));
                    break;
                default:
                    cq.orderBy(cb.desc(root.get("createdAt")));
            }
        } else {
            cq.orderBy(cb.desc(root.get("createdAt")));
        }

        TypedQuery<Product> query = entityManager.createQuery(cq);
        return query.getResultList();
    }


    /**
     * Retrieves all products without filters.
     *
     * @return List of all Product entities.
     */
    @Override
    public List<Product> getAllProducts() {
        return productJpaRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id ID of the product.
     * @return The found Product entity.
     * @throws ProductNotFoundException if the product is not found.
     */
    @Override
    public Product getById(Long id) {
        logger.info("Fetching product with ID: {}", id);
        return productJpaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product with ID {} not found", id);
                    return new ProductNotFoundException("Product with id " + id + " not found");
                });
    }

    /**
     * Creates a new product and saves it in the repository.
     *
     * @param product Product entity containing product details.
     * @return The created Product entity.
     */
    @Override
    public Product create(Product product) {
        logger.info("Creating new product: {}", product.getProductTitle());
        Product savedProduct = productJpaRepository.save(product);
        logger.info("Product created successfully with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    @Override
    public Product createProductDays(Long id) {
        String roleUser = userService.getCurrentUserRole();
        Product product = getById(id);
        String info = product.getProductInfo();
        if(roleUser.equals("ROLE_ADMIN")){
            product.setProductInfo(info + " Товар дня");
            productJpaRepository.save(product);
        }
        return product;
    }

    /**
     * Assigns a category to a product.
     *
     * @param productId ID of the product.
     * @param category Category entity to assign.
     * @return The updated Product entity.
     * @throws IllegalStateException if the product already belongs to a different category.
     */
    @Override
    public Product setCategory(Long productId, Category category) {
        Product product = getById(productId);
        if (category == null) {
            if (product.getCategory() != null) {
                product.setCategory(null);
                return productJpaRepository.save(product);
            } else {
                return product;
            }
        }
        if (product.getCategory() != null && !product.getCategory().equals(category)) {
            throw new IllegalStateException("Product already belongs to a Category");
        }
        product.setCategory(category);
        return productJpaRepository.save(product);
    }

    /**
     * Updates an existing product.
     *
     * @param id ID of the product to update.
     * @param productDto DTO containing updated product details.
     * @return The updated Product entity.
     * @throws BadArgumentsException if the product update fails due to invalid data.
     */
    @Override
    public Product editProducts(Long id, ProductCreateDto productDto) {
        logger.info("Updating product with ID: {}", id);
        Product actualProduct = getById(id);
        actualProduct.setProductTitle(productDto.getName());
        actualProduct.setProductInfo(productDto.getDescription());
        actualProduct.setPrice(productDto.getPrice());
        actualProduct.setCategory(categoryService.getByName(productDto.getCategory()));
        actualProduct.setDiscount(productDto.getDiscount());
        actualProduct.setUpdatedAt(productDto.getUpdateAt());
        actualProduct.setImageUrl(productDto.getImage());

        try {
            Product updatedProduct = productJpaRepository.save(actualProduct);
            logger.info("Product with ID: {} updated successfully", id);
            return updatedProduct;
        } catch (Exception e) {
            logger.error("Failed to update product with ID: {}", id);
            throw new BadArgumentsException("Form is not completed correctly");
        }
    }

    /**
     * Retrieves a product by its name.
     *
     * @param name Name of the product.
     * @return An Optional containing the found Product entity, or empty if not found.
     */
    @Override
    public Optional<Product> getByName(String name) {
        return productJpaRepository.findByName(name);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id ID of the product to delete.
     */
    @Override
    public void deleteProductsById(Long id) {
        logger.info("Deleting product with ID: {}", id);
        productJpaRepository.deleteById(id);
        logger.info("Product with ID: {} deleted successfully", id);
    }
}