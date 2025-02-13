package org.telran.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.OrderItemsCreateDto;
import org.telran.web.dto.OrderItemsResponseDto;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Orders;
import org.telran.web.entity.Product;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.OrderItemsService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
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
    @InjectMocks
    private OrderItemsController orderItemsController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Converter<OrderItems, OrderItemsCreateDto, OrderItemsResponseDto> converter;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
     @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteOrderItemTest() throws Exception {
        Long orderItemId = 1L;

        // Мокаем метод удаления
        doNothing().when(orderItemsService).deleteOrderItems(orderItemId);

        // Выполняем запрос
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/order_items/{id}", orderItemId)) // Изменено здесь
                .andExpect(MockMvcResultMatchers.status().isNoContent());  // Ожидаем статус 204 (No Content)
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getOrderItemsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order_items") // Изменено здесь
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
