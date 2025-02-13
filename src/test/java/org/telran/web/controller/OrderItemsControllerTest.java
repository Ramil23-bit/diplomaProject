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
//import org.telran.web.dto.OrderItemsCreateDto;
//import org.telran.web.dto.OrderItemsResponseDto;
//import org.telran.web.entity.OrderItems;
//import org.telran.web.exception.BadArgumentsException;
//import org.telran.web.security.JwtAuthenticationFilter;
//import org.telran.web.security.JwtService;
//import org.telran.web.service.OrderItemsService;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//@WebMvcTest(OrderItemsController.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class OrderItemsControllerTest {
//    @MockBean
//    private OrderItemsService orderItemsService;
//    @MockBean
//    private Converter<OrderItems, OrderItemsCreateDto, OrderItemsResponseDto> converter;
//    @MockBean
//    private JwtService jwtService;
//    @MockBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void createTest() throws Exception {
//        BigDecimal price = new BigDecimal(10);
//        OrderItemsCreateDto orderItemsCreateDto = new OrderItemsCreateDto(5L, 1L, 1L, price);
//        OrderItems orderItems = new OrderItems(orderItemsCreateDto.getOrderId(), orderItemsCreateDto.getQuantity(),
//                orderItemsCreateDto.getPriceAtPurchase(), null, null);
//        OrderItems newOrderItems = new OrderItems(1L, orderItems.getQuantity(), orderItems.getPriceAtPurchase());
//
//        when(converter.toEntity(orderItemsCreateDto))
//                .thenReturn(orderItems);
//        when(orderItemsService.createOrderItems(orderItems))
//                .thenReturn(newOrderItems);
//        when(converter.toDto(newOrderItems))
//                .thenReturn(new OrderItemsResponseDto(newOrderItems.getId(), newOrderItems.getQuantity(),
//                        newOrderItems.getPriceAtPurchase(), null, null));
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order_items")
//                        .content(asJsonString(orderItemsCreateDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//    }
//
//    @Test
//    void getAllTest() throws Exception {
//        OrderItems orderItems = new OrderItems(1L, 5L, new BigDecimal(10));
//        when(orderItemsService.getAllOrderItems())
//                .thenReturn(List.of(orderItems));
//        when(converter.toDto(orderItems))
//                .thenReturn(new OrderItemsResponseDto(orderItems.getId(), orderItems.getQuantity(),
//                        orderItems.getPriceAtPurchase(), null, null));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order_items")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(new OrderItemsResponseDto(
//                        orderItems.getId(), orderItems.getQuantity(), orderItems.getPriceAtPurchase(), null, null)))));
//    }
//
//    @Test
//    void getByIdWhenIdExists() throws Exception {
//        OrderItems orderItemsExists = new OrderItems(1L, 5L, new BigDecimal(10), null, null);
//        OrderItemsResponseDto orderItemsResponseDto = new OrderItemsResponseDto(orderItemsExists.getId(),
//                orderItemsExists.getQuantity(), orderItemsExists.getPriceAtPurchase(), null, null);
//        when(orderItemsService.getByIdOrderItems(orderItemsExists.getId()))
//                .thenReturn(orderItemsExists);
//        when(converter.toDto(orderItemsExists))
//                .thenReturn(orderItemsResponseDto);
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order_items/" + orderItemsExists.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(asJsonString(orderItemsResponseDto)));
//    }
//
//    @Test
//    void getByIdWhenIdNotExists() throws Exception {
//        OrderItems orderItemsExists = new OrderItems(1L, 5L, new BigDecimal(10), null, null);
//        when(orderItemsService.getByIdOrderItems(orderItemsExists.getId()))
//                .thenThrow(BadArgumentsException.class);
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order_items/" + orderItemsExists.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
//    }
//
//    @Test
//    void deleteDyIdWhenIdExists() throws Exception {
//        OrderItems orderItemsExists = new OrderItems(1L, 5L, new BigDecimal(10), null, null);
//        doNothing().when(orderItemsService).deleteOrderItems(orderItemsExists.getId());
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/order_items/" + orderItemsExists.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//
//        verify(orderItemsService).deleteOrderItems(orderItemsExists.getId());
//    }
//
//    @Test
//    void deleteByIdWhenIdDoesNotExist() throws Exception {
//        Long nonExistentId = 1L;
//
//        doThrow(BadArgumentsException.class).when(orderItemsService).deleteOrderItems(nonExistentId);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/order_items/" + nonExistentId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
//    }
//
//
//
//    private static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
