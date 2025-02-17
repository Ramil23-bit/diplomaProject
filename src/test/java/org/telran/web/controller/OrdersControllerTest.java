package org.telran.web.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.telran.web.converter.Converter;
import org.telran.web.converter.OrderCreateConverter;
import org.telran.web.converter.UserCreateConverter;
import org.telran.web.dto.OrderCreateDto;
import org.telran.web.dto.OrderResponseDto;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.OrdersService;
import org.telran.web.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrdersController.class)
@Import(OrderCreateConverter.class)
@AutoConfigureMockMvc
@EnableWebSecurity
public class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdersService ordersService;

    @MockBean
    private Converter<Order, OrderCreateDto, OrderResponseDto> converter;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserService userService;

    @MockBean
    private UserCreateConverter userCreateConverter;

    /**
     **Test Case:** Admin retrieves the list of all orders
     **Expected Result:** HTTP 200 (OK) with an empty list (if no orders exist)
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetOrdersForAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())  // HTTP 200 OK
                .andExpect(content().string(""))
                .andExpect(jsonPath("$").doesNotExist());
    }

    /**
     **Test Case:** Regular user retrieves the list of all orders
     **Expected Result:** HTTP 200 (OK) with an empty response
     */
    @Test
    @WithMockUser(roles = "USER")
    public void testGetOrdersForUser() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())  // HTTP 200 OK
                .andExpect(content().string(""))
                .andExpect(jsonPath("$").doesNotExist());
    }
}