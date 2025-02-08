//package org.telran.web.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.telran.web.converter.OrderCreateConverter;
//import org.telran.web.converter.OrderItemsConverter;
//import org.telran.web.dto.OrderCreateDto;
//import org.telran.web.dto.OrderResponseDto;
//import org.telran.web.entity.Orders;
//import org.telran.web.entity.User;
//import org.telran.web.enums.OrderStatus;
//import org.telran.web.service.OrdersService;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(OrdersController.class)
//class OrdersControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private OrdersService service;
//
//    @MockBean
//    private OrderCreateConverter converter;
//
//    @MockBean
//    private OrderItemsConverter orderItemsConverter;
//
//
//    private Orders mockOrder;
//    private OrderResponseDto mockResponse;
//
//    @Test
//    void testCreateOrder() throws Exception {
//        User mockUser = new User(1L, "testUser", "test@example.com", "password123", "+1234567890");
//
//        Orders mockOrder = new Orders(mockUser, "Test Address", "Courier");
//        mockOrder.setContactPhone("+1234567890");
//        mockOrder.setStatus(OrderStatus.CREATED);
//        mockOrder.setCreatedAt(LocalDateTime.now());
//        mockOrder.setUpdatedAt(LocalDateTime.now());
//
//        OrderResponseDto mockResponse = new OrderResponseDto(
//                1L, mockUser, List.of(), LocalDateTime.now(),
//                "Test Address", "+1234567890", "Courier",
//                OrderStatus.CREATED, LocalDateTime.now()
//        );
//
//        OrderCreateDto createDto = new OrderCreateDto(
//                1L,
//                List.of(),
//                "Test Address",
//                "Courier"
//        );
//
//        when(converter.toEntity(Mockito.any(OrderCreateDto.class))).thenReturn(mockOrder);
//        when(service.create(Mockito.any(Orders.class))).thenReturn(mockOrder);
//        when(converter.toDto(Mockito.any(Orders.class))).thenReturn(mockResponse);
//
//        mockMvc.perform(post("/api/v1/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.deliveryAddress", is("Test Address")))
//                .andExpect(jsonPath("$.deliveryMethod", is("Courier")));
//    }
//
//
//    @Test
//    void testGetAllOrders() throws Exception {
//        User mockUser = new User(1L, "testUser", "test@example.com", "password123", "+1234567890");
//
//        Orders mockOrder = new Orders(mockUser, "Main Street", "Courier");
//        mockOrder.setContactPhone("+1234567890");
//        mockOrder.setStatus(OrderStatus.CREATED);
//        mockOrder.setCreatedAt(LocalDateTime.now());
//        mockOrder.setUpdatedAt(LocalDateTime.now());
//
//        OrderResponseDto mockResponse = new OrderResponseDto(
//                1L, mockUser, List.of(), LocalDateTime.now(),
//                "Main Street", "+1234567890", "Courier",
//                OrderStatus.CREATED, LocalDateTime.now()
//        );
//
//        when(service.getAll()).thenReturn(List.of(mockOrder));
//        when(converter.toDto(Mockito.any(Orders.class))).thenReturn(mockResponse);
//
//        mockMvc.perform(get("/api/v1/orders"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].deliveryAddress", is("Main Street")));
//    }
//
//    @Test
//    void testGetOrderById() throws Exception {
//        User mockUser = new User(1L, "testUser", "test@example.com", "password123", "+1234567890");
//
//        Orders mockOrder = new Orders(mockUser, "Main Street", "Courier");
//        mockOrder.setContactPhone("+1234567890");
//        mockOrder.setStatus(OrderStatus.CREATED);
//        mockOrder.setCreatedAt(LocalDateTime.now());
//        mockOrder.setUpdatedAt(LocalDateTime.now());
//
//        OrderResponseDto mockResponse = new OrderResponseDto(
//                1L, mockUser, List.of(), LocalDateTime.now(),
//                "Main Street", "+1234567890", "Courier",
//                OrderStatus.CREATED, LocalDateTime.now()
//        );
//
//        when(service.getById(1L)).thenReturn(mockOrder);
//        when(converter.toDto(mockOrder)).thenReturn(mockResponse);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.deliveryAddress", is("Main Street")));
//    }
//
//}
//
