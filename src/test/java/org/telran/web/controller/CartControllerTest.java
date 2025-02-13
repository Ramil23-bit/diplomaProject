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
//import org.telran.web.dto.CartCreateDto;
//import org.telran.web.dto.CartResponseDto;
//import org.telran.web.dto.UserResponseDto;
//import org.telran.web.entity.Cart;
//import org.telran.web.security.AuthenticationService;
//import org.telran.web.security.JwtService;
//import org.telran.web.service.CartService;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@WebMvcTest(CartController.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class CartControllerTest {
//    @MockBean
//    private CartService cartService;
//    @MockBean
//    private Converter<Cart, CartCreateDto, CartResponseDto> converter;
//    @MockBean
//    private AuthenticationService authenticationService;
//    @MockBean
//    private JwtService jwtService;
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void createTest() throws Exception {
//        CartCreateDto cartCreateDto = new CartCreateDto(1L, 1L);
//        Cart cart = new Cart(1L, null);
//        Cart newCart = new Cart(cart.getId());
//
//        when(converter.toEntity(any(CartCreateDto.class))).thenReturn(cart);
//        when(cartService.createCart(any(Cart.class))).thenReturn(newCart);
//        when(converter.toDto(any(Cart.class))).thenReturn(new CartResponseDto(1L, new UserResponseDto(1L, "testUser", "test@example.com", "123456789")));
//
//        when(cartService.createCart(any(Cart.class))).thenReturn(newCart);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart")
//                        .content(asJsonString(cartCreateDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print()) // Выведет JSON-ответ в консоль
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value("testUser"));
//    }
//
//    private String asJsonString(final Object obj) {
//        try {
//            return objectMapper.writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}