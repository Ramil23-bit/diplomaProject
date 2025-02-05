package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CartCreateDto;
import org.telran.web.dto.CartResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.service.CartService;

import static org.mockito.Mockito.when;

@WebMvcTest(CartController.class)
public class CartControllerTest {
    @MockBean
    private CartService cartService;
    @MockBean
    private Converter<Cart, CartCreateDto, CartResponseDto> converter;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTest() throws Exception{
        CartCreateDto cartCreateDto = new CartCreateDto(1L, 1L);
        Cart cart = new Cart(1L, null);
        Cart newCart = new Cart(cart.getId());
        when(converter.toEntity(cartCreateDto))
                        .thenReturn(cart);
        when(cartService.createCart(cart))
                        .thenReturn(newCart);
        when(converter.toDto(newCart))
                        .thenReturn(new CartResponseDto(newCart.getId(), null));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
                        .content(asJsonString(cartCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
