package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Product;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.repository.ProductJpaRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductJpaRepository productJpaRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void getByIdWhenPruductExists() {
        Long productId = 3333333L;
        Product productExpected = new Product();
        productExpected.setId(3333333L);

        when(productJpaRepository.findById(productId))
                .thenReturn(Optional.of(productExpected));

        Product productActual = productService.getById(productId);

        assertEquals(productExpected.getId(), productActual.getId());
    }

    @Test
    public void getByIdWhenProductNotExists() {
        Long id = 4444444L;
        when(productJpaRepository.findById(id))
                .thenThrow(new ProductNotFoundException("Product not found"));

        assertThrows(ProductNotFoundException.class,
                () -> productService.getById(id));
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(1l, "Hammer"),
                new Product(2l, "Tomato"));
        when(productJpaRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAll();
        assertEquals(2, result.size());
        assertEquals("Hammer", result.get(0).getProductTitle());
        assertEquals("Tomato", result.get(1).getProductTitle());
    }

    @Test
    void create() {
        Product newProduct = new Product(null, "New Product");
        Product savedProduct = new Product(1L, "New Product");

        when(productJpaRepository.save(newProduct)).thenReturn(savedProduct);

        Product result = productService.create(newProduct);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Product", result.getProductTitle());
    }
}