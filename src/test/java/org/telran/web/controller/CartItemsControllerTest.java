package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CartItemsCreateDto;
import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.CartItems;
import org.telran.web.service.CartItemsService;
import org.telran.web.exception.BadArgumentsException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartItemsController.class)
public class CartItemsControllerTest {
    @MockBean
    private CartItemsService cartItemsService;

    @MockBean
    private Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> converter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTest() throws Exception {
        CartItemsCreateDto createDto = new CartItemsCreateDto(2L, 1L, 3L);
        CartItems cartItems = new CartItems(1L, 2L, null, null);
        CartItemsResponseDto responseDto = new CartItemsResponseDto(1L, 2L, null, null);

        when(converter.toEntity(createDto)).thenReturn(cartItems);
        when(cartItemsService.createCartItems(cartItems)).thenReturn(cartItems);
        when(converter.toDto(cartItems)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/cart_items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk());

    }

    @Test
    void getByIdWhenIdExists() throws Exception {
        CartItems cartItems = new CartItems(1L, 2L, null, null);
        CartItemsResponseDto responseDto = new CartItemsResponseDto(1L, 2L, null, null);

        when(cartItemsService.getByIdCartItems(1L)).thenReturn(cartItems);
        when(converter.toDto(cartItems)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/cart_items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void getByIdWhenIdDoesNotExist() throws Exception {
        when(cartItemsService.getByIdCartItems(1L)).thenThrow(BadArgumentsException.class);

        mockMvc.perform(get("/api/v1/cart_items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
    }
}
