//package org.telran.web.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.telran.web.converter.Converter;
//import org.telran.web.dto.ProductCreateDto;
//import org.telran.web.dto.ProductResponseDto;
//import org.telran.web.entity.Product;
//import org.telran.web.exception.BadArgumentsException;
//import org.telran.web.exception.ProductNotFoundException;
//import org.telran.web.service.ProductService;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//@WebMvcTest(ProductController.class)
//public class ProductControllerTest {
//    @MockBean
//    private ProductService productService;
//    @MockBean
//    private Converter<Product, ProductCreateDto, ProductResponseDto> converter;
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void createTest() throws Exception {
//        ProductCreateDto productCreateDto = new ProductCreateDto("title", new BigDecimal(10), "info",
//                1L, 1L, new BigDecimal(5), null);
//        Product product = new Product(1L, "title", new BigDecimal(10), "info",
//                new BigDecimal(5));
//        Product newProduct = new Product(product.getId(), product.getProductTitle(), product.getPrice(),
//                product.getProductInfo(), product.getDiscount());
//
//        when(converter.toEntity(productCreateDto))
//                .thenReturn(product);
//        when(productService.create(product))
//                .thenReturn(newProduct);
//        when(converter.toDto(newProduct))
//                .thenReturn(new ProductResponseDto(newProduct.getId(), newProduct.getProductTitle(),
//                        newProduct.getPrice(), newProduct.getDiscount(), newProduct.getCategoryId()));
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
//                        .content(asJsonString(productCreateDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//    }
//
//    @Test
//    void getAllTest() throws Exception {
//        Product product = new Product(1L, "title", new BigDecimal(10), "info",
//                new BigDecimal(5));
//        when(productService.getAll(1L, 1, new BigDecimal(5), new BigDecimal(10),
//                new BigDecimal(5)))
//                .thenReturn(List.of(product));
//        when(converter.toDto(product))
//                .thenReturn(new ProductResponseDto(product.getId(), product.getProductTitle(), product.getPrice(),
//                        product.getDiscount(), product.getCategoryId()));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    void getByIdWhenIdExists() throws Exception {
//        Product product = new Product(1L, "title", new BigDecimal(10), "info",
//                new BigDecimal(5));
//        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(), product.getProductTitle(),
//                product.getPrice(), product.getDiscount(), product.getCategoryId());
//        when(productService.getById(product.getId()))
//                .thenReturn(product);
//        when(converter.toDto(product))
//                .thenReturn(productResponseDto);
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/" + product.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(asJsonString(productResponseDto)));
//    }
//
//    @Test
//    void getByIdWhenIdNotExists() throws Exception {
//        Product product = new Product(1L, "title", new BigDecimal(10), "info",
//                new BigDecimal(5));
//        when(productService.getById(product.getId()))
//                .thenThrow(BadArgumentsException.class);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/" + product.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
//    }
//
//    @Test
//    void deleteDyIdWhenIdExists() throws Exception {
//        Product product = new Product(1L, "title", new BigDecimal(10), "info",
//                new BigDecimal(5));
//        doNothing().when(productService).deleteProductsById(product.getId());
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/" + product.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//
//        verify(productService).deleteProductsById(product.getId());
//    }
//
//    @Test
//    void deleteByIdWhenIdDoesNotExist() throws Exception {
//        Long nonExistentId = 1L;
//        doThrow(BadArgumentsException.class).when(productService).deleteProductsById(nonExistentId);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/" + nonExistentId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
//
//    }
//
//    @Test
//    void updateWhenProductDoesNotExist() throws Exception {
//        Long nonExistentId = 1L;
//        ProductCreateDto productCreateDto = new ProductCreateDto("Product", new BigDecimal(49.99),
//                "Description", 1L, 1L, new BigDecimal(5), null);
//
//        doThrow(ProductNotFoundException.class).when(productService).editProducts(eq(nonExistentId), any(ProductCreateDto.class));
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/" + nonExistentId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Product\",\"description\":\"Description\",\"price\":49.99}"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof ProductNotFoundException));
//    }
//
//    private static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
