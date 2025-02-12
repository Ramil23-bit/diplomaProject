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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.SecurityConfig;
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
@ActiveProfiles("test")
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class CartControllerTest {
    @MockBean
    private CartService cartService;
    @MockBean
    private Converter<Cart, CartCreateDto, CartResponseDto> converter;

    @Autowired
    private MockMvc mockMvc;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createCartAsUserTest() throws Exception {
        CartCreateDto cartCreateDto = new CartCreateDto(1L, 1L);
        Cart cart = new Cart(1L, null);
        CartResponseDto cartResponseDto = new CartResponseDto(1L, new UserResponseDto(1L, "user", "test@example.com", "123456789"));

        when(converter.toEntity(any(CartCreateDto.class))).thenReturn(cart);
        when(cartService.createCart(any(Cart.class))).thenReturn(cart);
        when(converter.toDto(any(Cart.class))).thenReturn(cartResponseDto);

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

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCurrentCartAsUserTest() throws Exception {
        Cart cart = new Cart(1L, null);
        CartResponseDto cartResponseDto = new CartResponseDto(1L, new UserResponseDto(1L, "user", "test@example.com", "123456789"));

        when(cartService.findByCurrentUser()).thenReturn(cart);
        when(converter.toDto(any(Cart.class))).thenReturn(cartResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart/current"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value("user"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getCurrentCartAsAdminTest() throws Exception {
        when(cartService.findByCurrentUser()).thenReturn(new Cart(1L, null));
        when(converter.toDto(any(Cart.class))).thenReturn(new CartResponseDto(1L, new UserResponseDto(1L, "admin", "admin@example.com", "123456789")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart/current"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    void getCurrentCartAsAnonymousTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart/current"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllCartsAsAdminTest() throws Exception {
        when(cartService.getAllCart()).thenReturn(Collections.singletonList(new Cart(1L, null)));
        when(converter.toDto(any(Cart.class))).thenReturn(new CartResponseDto(1L, new UserResponseDto(1L, "admin", "admin@example.com", "123456789")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllCartsAsUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void getAllCartsAsAnonymousTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


