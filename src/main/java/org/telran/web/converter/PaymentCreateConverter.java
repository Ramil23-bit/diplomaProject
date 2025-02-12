package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.PaymentCreateDto;
import org.telran.web.dto.PaymentResponseDto;
import org.telran.web.entity.Payment;
import org.telran.web.service.UserService;

/**
 * Converter class for transforming Payment entities to DTOs and vice versa.
 * Handles the conversion between Payment, PaymentCreateDto, and PaymentResponseDto.
 */
@Component
public class PaymentCreateConverter implements Converter<Payment, PaymentCreateDto, PaymentResponseDto> {

    @Autowired
    private UserService userService;

    /**
     * Converts a Payment entity to a PaymentResponseDto.
     *
     * @param payment The Payment entity to convert.
     * @return A PaymentResponseDto representing the payment.
     */
    @Override
    public PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(payment.getId(), payment.getUser());
    }

    /**
     * Converts a PaymentCreateDto to a Payment entity.
     *
     * @param paymentCreateDto The DTO containing payment creation data.
     * @return The created Payment entity.
     */
    @Override
    public Payment toEntity(PaymentCreateDto paymentCreateDto) {
        return new Payment(paymentCreateDto.getAmount(), userService.getById(paymentCreateDto.getUserId()));
    }
}