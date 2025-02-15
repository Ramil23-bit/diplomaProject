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
import org.telran.web.dto.CartCreateDto;
import org.telran.web.dto.CartResponseDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.CartService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CartController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import({TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private Converter<Cart, CartCreateDto, CartResponseDto> converter;

    @MockBean
    private UserDetailsService userDetailsService;

    // Set test profile for isolated configuration
    static {
        System.setProperty("spring.profiles.active", "test");
    }

    /**
     **Test Case:** User creates a new cart
     **Expected Result:** HTTP 201 (Created) with valid cart details
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createCartAsUserTest() throws Exception {
        // Mock request data
        CartCreateDto cartCreateDto = new CartCreateDto(1L, 1L);
        Cart cart = new Cart(1L, null);
        CartResponseDto cartResponseDto = new CartResponseDto(1L, new UserResponseDto(1L, "user", "test@example.com", "123456789"));

        // Mock service behavior
        when(cartService.createCart(any(Cart.class))).thenReturn(cart);
        when(converter.toEntity(any(CartCreateDto.class))).thenReturn(cart);
        when(converter.toDto(any(Cart.class))).thenReturn(cartResponseDto);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                        .content(asJsonString(cartCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value("user"));
    }

    /**
     **Test Case:** User retrieves their current cart
     **Expected Result:** HTTP 200 (OK) with valid cart details
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCurrentCartAsUserTest() throws Exception {
        // Mock cart data
        Cart cart = new Cart(1L, null);
        CartResponseDto cartResponseDto = new CartResponseDto(1L, new UserResponseDto(1L, "user", "test@example.com", "123456789"));

        // Mock service behavior
        when(cartService.findByCurrentUser()).thenReturn(cart);
        when(converter.toDto(any(Cart.class))).thenReturn(cartResponseDto);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart/current"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value("user"));
    }

    /**
     **Test Case:** Admin retrieves their current cart
     **Expected Result:** HTTP 200 (OK)
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getCurrentCartAsAdminTest() throws Exception {
        when(cartService.findByCurrentUser()).thenReturn(new Cart(1L, null));
        when(converter.toDto(any(Cart.class))).thenReturn(new CartResponseDto(1L, new UserResponseDto(1L, "admin", "admin@example.com", "123456789")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart/current"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     **Test Case:** Admin retrieves all carts
     **Expected Result:** HTTP 200 (OK) with list of carts
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllCartsAsAdminTest() throws Exception {
        when(cartService.getAllCart()).thenReturn(Collections.singletonList(new Cart(1L, null)));
        when(converter.toDto(any(Cart.class))).thenReturn(new CartResponseDto(1L, new UserResponseDto(1L, "admin", "admin@example.com", "123456789")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     **Test Case:** User attempts to retrieve all carts
     **Expected Result:** HTTP 403 (Forbidden)
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllCartsAsUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    /**
     **Test Case:** Anonymous user attempts to retrieve all carts
     **Expected Result:** HTTP 401 (Unauthorized)
     */
    @Test
    @WithAnonymousUser
    void getAllCartsAsAnonymousTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
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