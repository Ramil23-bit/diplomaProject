package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.PaymentCreateDto;
import org.telran.web.dto.PaymentResponseDto;
import org.telran.web.entity.Payment;
import org.telran.web.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Converter<Payment, PaymentCreateDto, PaymentResponseDto> paymentConverter;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto create(@RequestBody PaymentCreateDto paymentCreateDto){
        return paymentConverter.toDto(paymentService.createPayment(paymentConverter.toEntity(paymentCreateDto)));
    }

    @GetMapping("/{id}")
    public PaymentResponseDto getById(@PathVariable(name = "id") Long id){
        return paymentConverter.toDto(paymentService.getByIdPayment(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(name = "id") Long id){
        paymentService.deletePayment(id);
    }
}
