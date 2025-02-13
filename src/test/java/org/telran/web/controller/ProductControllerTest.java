//package org.telran.web.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
//import org.telran.web.security.JwtService;
//import org.telran.web.service.ProductService;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//@WebMvcTest(ProductController.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class ProductControllerTest {
//    @MockBean
//    private ProductService productService;
//    @MockBean
//    private Converter<Product, ProductCreateDto, ProductResponseDto> converter;
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private JwtService jwtService;
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
//    void deleteByIdWhenIdExists() throws Exception {
//        Long id = 1L;
//        doNothing().when(productService).deleteProductsById(id);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/" + id)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isNoContent());  // Теперь ожидаемый статус 204
//
//        verify(productService).deleteProductsById(id);
//    }
//
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
