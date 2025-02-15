package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.PaymentCreateDto;
import org.telran.web.dto.PaymentResponseDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.Payment;
import org.telran.web.entity.User;
import org.telran.web.enums.OrderStatus;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.PaymentService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PaymentController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import({TestSecurityConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private Converter<Payment, PaymentCreateDto, PaymentResponseDto> converter;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    /**
     **Test Case:** Retrieve a payment by ID when it exists
     **Expected Result:** HTTP 200 (OK) with valid payment details
     */
    @Test
    @WithMockUser(roles = "USER")
    void getByIdWhenIdIsExists() throws Exception {
        // Mock user data
        User user = new User(1L, "user", "test@example.com", "password123", "123456789");

        // Mock payment entity
        Payment existsPayment = new Payment(1L, 5L, null, LocalDateTime.now(), OrderStatus.CREATED);

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto(existsPayment.getId(), user);

        // Mock service behavior
        when(paymentService.getByIdPayment(existsPayment.getId())).thenReturn(existsPayment);
        when(converter.toDto(existsPayment)).thenReturn(paymentResponseDto);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payment/" + existsPayment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())  // HTTP 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))  // ID must be 1
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value("user"))  // Username must match
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email").value("test@example.com"));  // Email must match
    }

    /**
     **Test Case:** Retrieve a payment by ID when it does not exist
     **Expected Result:** HTTP 400 (Bad Request) with an exception
     */
    @Test
    @WithMockUser(roles = "USER")
    void getByIdWhenIdIsNotExists() throws Exception {
        // Define test payment ID
        Payment existsPayment = new Payment(1L, 5L, null, LocalDateTime.now(), OrderStatus.CREATED);

        // Mock service behavior for non-existent payment
        when(paymentService.getByIdPayment(existsPayment.getId())).thenThrow(BadArgumentsException.class);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/payment/" + existsPayment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  // HTTP 400 Bad Request
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));  // Exception must be BadArgumentsException
    }

    /**
     **Test Case:** Delete a payment by ID when it exists
     **Expected Result:** HTTP 204 (No Content) with successful deletion
     */
    @Test
    @WithMockUser(roles = "USER")
    void deleteByIdWhenIdExists() throws Exception {
        // Define test payment ID
        Payment existsPayment = new Payment(1L, 5L, null, LocalDateTime.now(), OrderStatus.CREATED);

        // Mock service behavior
        Mockito.doNothing().when(paymentService).deletePayment(existsPayment.getId());

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/payment/" + existsPayment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());  // HTTP 204 No Content

        // Verify that the service method was called once
        verify(paymentService).deletePayment(existsPayment.getId());
    }

    /**
     **Test Case:** Delete a payment by ID when it does not exist
     **Expected Result:** HTTP 400 (Bad Request) with an exception
     */
    @Test
    @WithMockUser(roles = "USER")
    void deleteByIdWhenIdDoesNotExist() throws Exception {
        // Define non-existent payment ID
        Long nonExistentId = 1L;

        // Mock service behavior for non-existent payment
        doThrow(BadArgumentsException.class).when(paymentService).deletePayment(nonExistentId);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/payment/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  // HTTP 400 Bad Request
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));  // Exception must be BadArgumentsException
    }

    /**
     * Utility method to convert objects to JSON string.
     * @param obj Object to be converted
     * @return JSON string representation
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}