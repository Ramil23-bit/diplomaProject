package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.entity.Storage;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.repository.ProductJpaRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private static final Storage storage = new Storage(1L, 1L, new ArrayList<>());
    private static final List<Product> PRODUCTS = Arrays.asList(
            new Product(
                    3L,
                    "Axe",
                    BigDecimal.valueOf(1),
                    "Hand tool for chopping wood",
                    new Category(1L, "Tools and equipment", new ArrayList<>()),
                    storage,
                    BigDecimal.ZERO,
                    null,
                    null
            ),

            new Product(
                    4L,
                    "Drill",
                    BigDecimal.valueOf(4),
                    "Electric drill for construction work",
                    new Category(1L, "Tools and equipment", new ArrayList<>()),
                    storage,
                    BigDecimal.ZERO,
                    null,
                    null
            ),
            new Product(
                    5L,
                    "Blower",
                    BigDecimal.valueOf(2),
                    "Electric blower for garden leaves",
                    new Category(1L, "Tools and equipment", new ArrayList<>()),
                    storage,
                    BigDecimal.ZERO,
                    null,
                    null
            ),
            new Product(
                    6L,
                    "Excavator",
                    BigDecimal.valueOf(5),
                    "Mini excavator for landscaping",
                    new Category(1L, "Tools and equipment", new ArrayList<>()),
                    storage,
                    BigDecimal.ZERO,
                    null,
                    null
            ),
            new Product(
                    7L,
                    "Chainsaw",
                    BigDecimal.valueOf(3),
                    "Powerful chainsaw for cutting trees",
                    new Category(1L, "Tools and equipment", new ArrayList<>()),
                    storage,
                    BigDecimal.ZERO,
                    null,
                    null
            ));

    @Mock
    private ProductJpaRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    public void getAllProducts() {

        Product productOne = PRODUCTS.get(0);
        Product productTwo = PRODUCTS.get(1);

        List<Product> productsFromMock = Arrays.asList(productOne, productTwo);
        when(repository.findAll()).thenReturn(productsFromMock);
        List<Product> products = service.getAll();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(productOne, products.get(0));
        assertEquals(productTwo, products.get(1));
    }
    @Test
    public void getProductByIdWhenProductExists() {
        Long productId = 1L;
        Product productOne = PRODUCTS.get(0);
        when(repository.findById(productId))
                .thenReturn((Optional.of(productOne)));
        Product productActual = service.getById(productId);
        assertNotNull(productActual);
        assertEquals(productOne, productActual);
    }

    @Test
    public void getProductByIdWhenProductNotExists() {
        Long productId = 1L;
        when(repository.findById(productId))
                .thenThrow(new ProductNotFoundException("Product not found"));
        assertThrows(ProductNotFoundException.class,
                () -> service.getById(productId));
    }

    @Test
    void createProduct() {
        Product product = PRODUCTS.get(0);
        product.setId(null);
        Product savedProduct = PRODUCTS.get(0);
        savedProduct.setId(1L);

        when(repository.save(product)).thenReturn(savedProduct);
        Product createdProduct = service.create(product);

        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId());
        assertEquals(1L, createdProduct.getId());
    }
}