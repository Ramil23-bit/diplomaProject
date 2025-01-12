package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
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

    @Mock
    private ProductJpaRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    public void getAllProducts() {

        Product productOne = createProductList().get(0);
        Product productTwo = createProductList().get(1);

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
        Product productOne = createProductList().get(0);
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
        Product product = createProductList().get(0);
        product.setId(null);
        Product savedProduct = createProductList().get(0);

        when(repository.save(product)).thenReturn(savedProduct);
        Product createdProduct = service.create(product);

        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId());
        assertEquals(1L, createdProduct.getId());

    }

    private List<Product> createProductList(){
        return Arrays.asList(
                new Product(
                        1L,
                        "Trimmer",
                        BigDecimal.valueOf(250),
                        "Electric trimmer",
                        new Category(1L, "Tools and equipment", new ArrayList<>()),
                        BigDecimal.ZERO,
                        null,
                        null),
                new Product(
                        2L,
                        "Nitrogen",
                        BigDecimal.valueOf(50),
                        "For green leafy growth",
                        new Category(2L, "Fertilizer", new ArrayList<>()),
                        BigDecimal.ZERO,
                        null,
                        null
                )
        );
    }
}