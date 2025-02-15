package org.telran.web.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.entity.Storage;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.repository.ProductJpaRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *   Key Features:
 * - Uses `@ExtendWith(MockitoExtension.class)` for Mockito-based testing.
 * - Mocks `ProductJpaRepository` and `EntityManager` to isolate business logic.
 * - Ensures **handling of existing and non-existing products**.
 * - Verifies **proper use of CriteriaBuilder for dynamic filtering**.
 */

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<Product> criteriaQuery;
    @Mock
    private Root<Product> root;
    @Mock
    private TypedQuery<Product> typedQuery;
    @Mock
    private ProductJpaRepository repository;
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final Storage storage = new Storage(1L, 1L);
    private static final List<Product> PRODUCTS = Arrays.asList(
            new Product(3L, "Axe", BigDecimal.valueOf(1), "Hand tool for chopping wood",
                    new Category(1L, "Tools and equipment", new ArrayList<>()), storage, BigDecimal.ZERO, null, null),
            new Product(4L, "Drill", BigDecimal.valueOf(4), "Electric drill for construction work",
                    new Category(1L, "Tools and equipment", new ArrayList<>()), storage, BigDecimal.ZERO, null, null)
    );

    /**
     **Test Case:** Retrieve all products with filtering using CriteriaBuilder.
     **Expected Result:** Returns a filtered list of products.
     */
    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void testGetAllWithCriteriaBuilder() {
        Long categoryId = 1L;
        int direction = 1;
        BigDecimal minPrice = BigDecimal.valueOf(2);
        BigDecimal maxPrice = BigDecimal.valueOf(5);
        BigDecimal discount = BigDecimal.valueOf(10);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Product.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Product.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(new ArrayList<>(PRODUCTS));

        when(root.get("category")).thenReturn(mock(Path.class));
        when(root.get("category").get("id")).thenReturn(mock(Path.class));

        List<Product> result = service.getAll(categoryId, direction, minPrice, maxPrice, discount, "createdAt");

        assertNotNull(result);
        assertEquals(PRODUCTS.size(), result.size());
    }


    /**
     **Test Case:** Retrieve an existing product by ID.
     **Expected Result:** Returns the correct product details.
     */
    @Test
    void getProductByIdWhenProductExists() {
        Long productId = 3L;
        Product expectedProduct = PRODUCTS.get(0);

        when(repository.findById(productId)).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = service.getById(productId);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
    }

    /**
     **Test Case:** Attempt to retrieve a non-existing product by ID.
     **Expected Result:** Throws `ProductNotFoundException`.
     */
    @Test
    void getProductByIdWhenProductNotExists() {
        Long productId = 10L;
        when(repository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.getById(productId));
    }

    /**
     **Test Case:** Successfully create a new product.
     **Expected Result:** The product is saved and returned correctly.
     */
    @Test
    void createProduct() {
        Product product = PRODUCTS.get(0);
        product.setId(null);
        Product savedProduct = new Product(1L, "Axe", BigDecimal.valueOf(1), "Hand tool",
                new Category(1L, "Tools", new ArrayList<>()), storage, BigDecimal.ZERO, null, null);

        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        Product createdProduct = service.create(product);

        assertNotNull(createdProduct);
        assertEquals(1L, createdProduct.getId());
    }

    /**
     **Test Case:** Update an existing product.
     **Expected Result:** The product is updated with new values.
     */
    @Test
    void editProductWhenProductExists() {
        Long productId = 3L;
        Product existingProduct = PRODUCTS.get(0);

        ProductCreateDto updateDto = new ProductCreateDto();
        updateDto.setPrice(BigDecimal.valueOf(300));
        updateDto.setDescription("New Electric Trimmer");
        updateDto.setDiscount(BigDecimal.valueOf(10));
        updateDto.setUpdateAt(java.time.LocalDateTime.now());
        updateDto.setCategory("Tools");

        when(repository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setUpdatedAt(updateDto.getUpdateAt());
            return savedProduct;
        });
        when(categoryService.getByName(updateDto.getCategory())).thenReturn(new Category(1L, "Tools"));

        Product updatedProduct = service.editProducts(productId, updateDto);

        assertNotNull(updatedProduct);
        assertEquals(updateDto.getPrice(), updatedProduct.getPrice());
        assertEquals(updateDto.getDescription(), updatedProduct.getProductInfo());
        assertEquals(updateDto.getDiscount(), updatedProduct.getDiscount());
        assertEquals(updateDto.getUpdateAt(), updatedProduct.getUpdatedAt());
    }

    /**
     **Test Case:** Attempt to update a non-existing product.
     **Expected Result:** Throws `ProductNotFoundException`.
     */
    @Test
    void editProductWhenProductDoesNotExist() {
        Long productId = 3L;
        ProductCreateDto updateDto = new ProductCreateDto();
        updateDto.setPrice(BigDecimal.valueOf(300));

        when(repository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.editProducts(productId, updateDto));
    }
}