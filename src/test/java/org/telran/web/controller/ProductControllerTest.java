package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Product;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.exception.ProductNotFoundException;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ProductController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import({TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private Converter<Product, ProductCreateDto, ProductResponseDto> converter;

    @MockBean
    private UserDetailsService userDetailsService;

    /**
     **Test Case:** Retrieve all products
     **Expected Result:** HTTP 200 (OK) with product list
     */
    @Test
    @WithMockUser(roles = "USER")
    void getAllTest() throws Exception {
        // Mock product entity
        Product product = new Product(1L, "title", new BigDecimal(10), "info", new BigDecimal(5));

        // Mock service behavior
        when(productService.getAll(1L, 1, new BigDecimal(5), new BigDecimal(10), new BigDecimal(5), "createdAt"))
                .thenReturn(List.of(product));

        when(converter.toDto(product))
                .thenReturn(new ProductResponseDto(product.getId(), product.getProductTitle(), product.getPrice(),
                        product.getDiscount(), product.getCategoryId()));

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());  // HTTP 200 OK
    }

    /**
     **Test Case:** Retrieve product by ID when it exists
     **Expected Result:** HTTP 200 (OK) with valid product details
     */
    @Test
    @WithMockUser(roles = "USER")
    void getByIdWhenIdExists() throws Exception {
        // Mock product entity
        Product product = new Product(1L, "title", new BigDecimal(10), "info", new BigDecimal(5));

        // Mock DTO
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(), product.getProductTitle(),
                product.getPrice(), product.getDiscount(), product.getCategoryId());

        // Mock service behavior
        when(productService.getById(product.getId())).thenReturn(product);
        when(converter.toDto(product)).thenReturn(productResponseDto);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())  // HTTP 200 OK
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":1,\"productTitle\":\"title\",\"price\":10,\"discount\":5}"));  // JSON matches expected structure
    }

    /**
     **Test Case:** Retrieve product by ID when it does not exist
     **Expected Result:** HTTP 400 (Bad Request) with an exception
     */
    @Test
    @WithMockUser(roles = "USER")
    void getByIdWhenIdNotExists() throws Exception {
        // Mock product entity
        Product product = new Product(1L, "title", new BigDecimal(10), "info", new BigDecimal(5));

        // Mock service behavior for non-existent product
        when(productService.getById(product.getId())).thenThrow(BadArgumentsException.class);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  // HTTP 400 Bad Request
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));  // Exception must be BadArgumentsException
    }

    /**
     **Test Case:** Delete product by ID when it exists
     **Expected Result:** HTTP 204 (No Content) with successful deletion
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteByIdWhenIdExists() throws Exception {
        // Define test product ID
        Long id = 1L;

        // Mock service behavior
        doNothing().when(productService).deleteProductsById(id);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());  // HTTP 204 No Content

        // Verify that the service method was called once
        verify(productService).deleteProductsById(id);
    }

    /**
     **Test Case:** Delete product by ID when it does not exist
     **Expected Result:** HTTP 400 (Bad Request) with an exception
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteByIdWhenIdDoesNotExist() throws Exception {
        // Define non-existent product ID
        Long nonExistentId = 1L;

        // Mock service behavior for non-existent product
        doThrow(BadArgumentsException.class).when(productService).deleteProductsById(nonExistentId);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  // HTTP 400 Bad Request
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));  // Exception must be BadArgumentsException
    }

    /**
     **Test Case:** Update product when it does not exist
     **Expected Result:** HTTP 404 (Not Found) with an exception
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateWhenProductDoesNotExist() throws Exception {
        // Define non-existent product ID
        Long nonExistentId = 1L;

        // Define test product DTO
        ProductCreateDto productCreateDto = new ProductCreateDto("Product", new BigDecimal(49.99),
                "Description", 1L, 1L, new BigDecimal(5), null);

        // Mock service behavior
        doThrow(ProductNotFoundException.class).when(productService).editProducts(eq(nonExistentId), any(ProductCreateDto.class));

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Product\",\"description\":\"Description\",\"price\":49.99}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())  // HTTP 404 Not Found
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof ProductNotFoundException));  // ✅ Exception must be ProductNotFoundException
    }

    /**
     * Utility method to convert objects to JSON string.
     * @param obj Object to be converted
     * @return JSON string representation
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}