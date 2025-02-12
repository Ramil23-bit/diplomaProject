package org.telran.web.controller;

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
 * Provides endpoints for creating, retrieving, and deleting payments.
 */
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Converter<Payment, PaymentCreateDto, PaymentResponseDto> paymentConverter;

    /**
     * Creates a new payment.
     *
     * @param paymentCreateDto DTO containing the payment details.
     * @return PaymentResponseDto representing the created payment.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto create(@RequestBody PaymentCreateDto paymentCreateDto){
        return paymentConverter.toDto(paymentService.createPayment(paymentConverter.toEntity(paymentCreateDto)));
    }

    /**
     * Retrieves a payment by its ID. Only accessible by admins.
     *
     * @param id ID of the payment.
     * @return PaymentResponseDto representing the found payment.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponseDto getById(@PathVariable(name = "id") Long id){
        return paymentConverter.toDto(paymentService.getByIdPayment(id));
    }

    /**
     * Deletes a payment by its ID. Only accessible by admins.
     *
     * @param id ID of the payment to delete.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(name = "id") Long id){
        paymentService.deletePayment(id);
    }
}
