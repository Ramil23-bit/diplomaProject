package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.PaymentCreateDto;
import org.telran.web.dto.PaymentResponseDto;
import org.telran.web.entity.Payment;
import org.telran.web.service.PaymentService;

/**
 * Controller for managing payments.
 * Provides endpoints to create, retrieve, and delete payments.
 */
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Converter<Payment, PaymentCreateDto, PaymentResponseDto> paymentConverter;

    /**
     * Creates a new payment.
     *
     * @param paymentCreateDto The DTO containing payment details.
     * @return The created payment response DTO.
     */
    @Operation(summary = "Create a new payment", description = "Processes a new payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto create(@RequestBody PaymentCreateDto paymentCreateDto) {
        logger.info("Received request to create payment: {}", paymentCreateDto);
        PaymentResponseDto response = paymentConverter.toDto(paymentService.createPayment(paymentConverter.toEntity(paymentCreateDto)));
        logger.info("Payment created successfully with ID: {}", response.getId());
        return response;
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param id The ID of the payment.
     * @return The payment response DTO.
     */
    @Operation(summary = "Get payment by ID", description = "Retrieves details of a specific payment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponseDto getById(@PathVariable(name = "id") Long id) {
        logger.info("Fetching payment with ID: {}", id);
        PaymentResponseDto payment = paymentConverter.toDto(paymentService.getByIdPayment(id));
        logger.info("Payment retrieved: {}", payment);
        return payment;
    }

    /**
     * Deletes a payment by its ID.
     *
     * @param id The ID of the payment to delete.
     */
    @Operation(summary = "Delete payment by ID", description = "Removes a specific payment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(name = "id") Long id) {
        logger.info("Request to delete payment with ID: {}", id);
        paymentService.deletePayment(id);
        logger.info("Payment with ID {} successfully deleted", id);
    }
}
