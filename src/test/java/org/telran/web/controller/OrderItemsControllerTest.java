package org.telran.web.controller;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.OrderItemsCreateDto;
import org.telran.web.dto.OrderItemsResponseDto;
import org.telran.web.entity.OrderItems;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.OrderItemsService;

import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = OrderItemsController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import({TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderItemsControllerTest {

    @MockBean
    private OrderItemsService orderItemsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Converter<OrderItems, OrderItemsCreateDto, OrderItemsResponseDto> converter;

    @MockBean
    private UserDetailsService userDetailsService;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    /**
     **Test Case:** Admin deletes an order item by ID
     **Expected Result:** HTTP 204 (No Content)
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteOrderItemTest() throws Exception {
        Long orderItemId = 1L;
        doNothing().when(orderItemsService).deleteOrderItems(orderItemId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/order_items/{id}", orderItemId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     **Test Case:** User retrieves order items
     **Expected Result:** HTTP 200 (OK)
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getOrderItemsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order_items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}