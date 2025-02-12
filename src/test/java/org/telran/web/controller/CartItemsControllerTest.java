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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.SecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CartItemsCreateDto;
import org.telran.web.dto.CartItemsResponseDto;
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
@ActiveProfiles("test")
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class CartItemsControllerTest {

    @MockBean
    private CartItemsService cartItemsService;

    @MockBean
    private Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> cartItemsConverter;

    @Autowired
    private MockMvc mockMvc;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    private final Category category = new Category(1L, "Electronics");
    private final Storage storage = new Storage(1L, 100L);
    private final Product product = new Product(1L, "ProductName", new BigDecimal("99.99"), "Some description", category, storage, new BigDecimal("10.00"));
    private final User user = new User("TestUser", "testuser", "test@example.com", "123456789");
    private final Cart cart = new Cart(1L, user);
    private final CartItems cartItems = new CartItems(1L, 1L, cart, product);
    private final CartItemsResponseDto cartItemsResponseDto =
            new CartItemsResponseDto(1L, 1L, product, new UserResponseDto(1L, "TestUser", "testuser@example.com", "123456789"));


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createCartItemAsUserTest() throws Exception {
        // Создаём тестовые данные
        CartItemsCreateDto cartItemsCreateDto = new CartItemsCreateDto(1L, 1L, 2L);
        Category category = new Category(1L, "Electronics");
        Storage storage = new Storage(1L, 100L);
        Product product = new Product(1L, "ProductName", new BigDecimal("99.99"), "Some description", category, storage, new BigDecimal("10.00"));
        User user = new User("TestUser", "testuser", "test@example.com", "123456789");

        Cart cart = new Cart(1L, user);

        CartItems cartItems = new CartItems(1L, 2L, cart, product);

        CartItemsResponseDto cartItemsResponseDto = new CartItemsResponseDto(1L, 2L, product,
                new UserResponseDto(1L, "TestUser", "testuser@example.com", "123456789")
        );


        when(cartItemsConverter.toEntity(any(CartItemsCreateDto.class))).thenReturn(cartItems);
        when(cartItemsService.createCartItems(any(CartItems.class))).thenReturn(cartItems);
        when(cartItemsConverter.toDto(any(CartItems.class))).thenReturn(cartItemsResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cart_items")
                        .content(asJsonString(cartItemsCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2)); // Ожидали 2, теперь должно совпасть
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllCartItemsAsAdminTest() throws Exception {
        Category category = new Category(1L, "Electronics");
        Storage storage = new Storage(1L, 100L);

        Product product = new Product(1L, "ProductName", new BigDecimal("99.99"), "Some description", category, storage, new BigDecimal("10.00"));


        User user = new User(1L, "AdminUser", "adminuser", "admin@example.com", "123456789");
        Cart cart = new Cart(1L, user);

        CartItems cartItems = new CartItems(1L, 1L, cart, product);

        // Теперь user в CartItemsResponseDto тоже имеет id = 1L
        CartItemsResponseDto cartItemsResponseDto = new CartItemsResponseDto(1L, 1L, product, new UserResponseDto(1L, "AdminUser", "adminuser@example.com", "123456789"));

        when(cartItemsService.getAllCartItems()).thenReturn(Collections.singletonList(cartItems));
        when(cartItemsConverter.toDto(any(CartItems.class))).thenReturn(cartItemsResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart_items"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].product.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].user.id").value(1L)); // Теперь user.id должен быть 1
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCartItemByIdTest() throws Exception {
        // Создаем тестовые данные
        Category category = new Category(1L, "Electronics");
        Storage storage = new Storage(1L, 100L);

        Product product = new Product(1L, "ProductName", new BigDecimal("99.99"), "Some description", category, storage, new BigDecimal("10.00"));

        User user = new User("TestUser", "testuser@example.com", "testpassword", "123456789");
        user.setId(1L);

        Cart cart = new Cart(1L, user);
        CartItems cartItems = new CartItems(1L, 1L, cart, product);

        UserResponseDto userResponseDto = new UserResponseDto(1L, "TestUser", "testuser@example.com", "123456789");
        CartItemsResponseDto cartItemsResponseDto = new CartItemsResponseDto(1L, 1L, product, userResponseDto);


        when(cartItemsService.getByIdCartItems(1L)).thenReturn(cartItems);
        when(cartItemsConverter.toDto(any(CartItems.class))).thenReturn(cartItemsResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cart_items/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(1L)); // user.id больше не null!
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

