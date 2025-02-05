package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.telran.web.dto.PaymentCreateDto;
import org.telran.web.dto.PaymentResponseDto;
import org.telran.web.entity.Payment;
import org.telran.web.enums.OrderStatus;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.service.PaymentService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {
    @MockBean
    private PaymentService paymentService;
    @MockBean
    private Converter<Payment, PaymentCreateDto, PaymentResponseDto> converter;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTest() throws Exception {
        PaymentCreateDto paymentCreateDto = new PaymentCreateDto(5L, 1L);
        Payment payment = new Payment(paymentCreateDto.getUserId(), paymentCreateDto.getAmount(), null, LocalDateTime.now(), OrderStatus.CREATED);
        Payment newPayment = new Payment(1L, payment.getAmount(), null, payment.getDate(), payment.getOrderStatus());
        when(converter.toEntity(paymentCreateDto))
                .thenReturn(payment);
        when(paymentService.createPayment(payment))
                .thenReturn(newPayment);
        when(converter.toDto(newPayment))
                .thenReturn(new PaymentResponseDto(newPayment.getId(), null));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payment")
                        .content(asJsonString(paymentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void getByIdWhenIdIsExists() throws Exception{
        Payment existsPayment = new Payment(1L, 5L, null, LocalDateTime.now(),OrderStatus.CREATED);
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto(existsPayment.getId(), null);
        when(paymentService.getByIdPayment(existsPayment.getId()))
                .thenReturn(existsPayment);
        when(converter.toDto(existsPayment))
                .thenReturn(paymentResponseDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payment/" + existsPayment.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(paymentResponseDto)));
    }

    @Test
    void getByIdWhenIdIsNotExists() throws Exception{
        Payment existsPayment = new Payment(1L, 5L, null, LocalDateTime.now(),OrderStatus.CREATED);
        when(paymentService.getByIdPayment(existsPayment.getId()))
                .thenThrow(BadArgumentsException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payment/" + existsPayment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
    }

    @Test
    void deleteDyIdWhenIdExists()throws Exception{
        Payment existsPayment = new Payment(1L, 5L, null, LocalDateTime.now(),OrderStatus.CREATED);
        Mockito.doNothing().when(paymentService).deletePayment(existsPayment.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/payment/" + existsPayment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(paymentService).deletePayment(existsPayment.getId());
    }
    @Test
    void deleteByIdWhenIdDoesNotExist() throws Exception {
        Long nonExistentId = 1L;

        doThrow(BadArgumentsException.class).when(paymentService).deletePayment(nonExistentId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/payment/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
    }



    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
