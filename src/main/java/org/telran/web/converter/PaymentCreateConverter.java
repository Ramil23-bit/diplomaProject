package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.PaymentCreateDto;
import org.telran.web.dto.PaymentResponseDto;
import org.telran.web.entity.Payment;
import org.telran.web.service.UserService;
@Component
public class PaymentCreateConverter implements Converter<Payment, PaymentCreateDto, PaymentResponseDto>{

    @Autowired
    private UserService userService;
    @Override
    public PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(payment.getId(), payment.getUser());
    }

    @Override
    public Payment toEntity(PaymentCreateDto paymentCreateDto) {
        return new Payment(paymentCreateDto.getAmount(), userService.getById(paymentCreateDto.getUserId()));
    }
}
