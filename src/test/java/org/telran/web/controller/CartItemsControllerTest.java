package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CartItemsCreateDto;
import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.*;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.CartItemsService;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CartItemsController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CartItemsControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(CartItemsControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartItemsService cartItemsService;

    @MockBean
    private Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> cartItemsConverter;

    @MockBean
    private UserDetailsService userDetailsService;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    /**
     **Test Case:** User retrieves their current cart items
     **Expected Result:** HTTP 200 (OK) with valid cart item details
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCurrentCartItemsAsUserTest() throws Exception {
        // Mock category, storage, product, and cart items
        Category category = new Category(1L, "Category1");
        Storage storage = new Storage(1L, 100L);
        Cart cart = new Cart(1L);
        Product product = new Product(1L, "Product Title", new BigDecimal("19.99"), "Product Info", category, storage, new BigDecimal("5.00"));
        CartItems cartItems = new CartItems(1L, cart, product);

        // Mock service response
        when(cartItemsService.getAllByCurrentUser()).thenReturn(Collections.singletonList(cartItems));

        // Mock DTO conversion
        ProductResponseDto productResponseDto = new ProductResponseDto(1L, "Product Name", new BigDecimal("100.00"), new BigDecimal("10.00"), 1L);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "user", "user@example.com", "123456789");
        CartItemsResponseDto cartItemsResponseDto = new CartItemsResponseDto(1L, 1L, productResponseDto, userResponseDto);
        when(cartItemsConverter.toDto(any(CartItems.class))).thenReturn(cartItemsResponseDto);

        logger.info("Mocked CartItemsResponseDto: {}", cartItemsResponseDto);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart_items/current"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].product.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(1L));
    }

    /**
     **Test Case:** Admin retrieves all cart items
     **Expected Result:** HTTP 200 (OK) with a list of cart items
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllCartItemsAsAdminTest() throws Exception {
        // Mock category, storage, product, and cart items
        Category category = new Category(1L, "Category1");
        Storage storage = new Storage(1L, 100L);
        Cart cart = new Cart(1L);
        Product product = new Product(1L, "Product Title", new BigDecimal("19.99"), "Product Info", category, storage, new BigDecimal("5.00"));
        CartItems cartItems = new CartItems(1L, cart, product);

        // Mock service response
        when(cartItemsService.getAllCartItems()).thenReturn(Collections.singletonList(cartItems));

        // Mock DTO conversion
        ProductResponseDto productResponseDto = new ProductResponseDto(1L, "Product Name", new BigDecimal("100.00"), new BigDecimal("10.00"), 1L);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "user", "user@example.com", "123456789");
        CartItemsResponseDto cartItemsResponseDto = new CartItemsResponseDto(1L, 1L, productResponseDto, userResponseDto);
        when(cartItemsConverter.toDto(any(CartItems.class))).thenReturn(cartItemsResponseDto);

        logger.info("Mocked CartItemsResponseDto: {}", cartItemsResponseDto);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart_items"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].product.id").value(1L))  // Verify product ID
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(1L));  // Verify quantity
    }

    /**
     **Test Case:** Anonymous user attempts to retrieve all cart items
     **Expected Result:** HTTP 401 (Unauthorized)
     */
    @Test
    @WithAnonymousUser
    void getAllCartItemsAsAnonymousTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart_items"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    /**
     **Test Case:** User deletes a cart item by ID
     **Expected Result:** HTTP 200 (OK)
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteCartItemByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cart_items/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
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
