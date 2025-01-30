package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.OrderCreateConverter;
import org.telran.web.dto.*;
import org.telran.web.service.OrdersService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    @Autowired
    private OrdersService service;

    @Autowired
    private OrderCreateConverter converter;

    @PostMapping
    public OrderResponseDto create(@Valid @RequestBody OrderCreateDto dto) {
        return null;
    }

    @GetMapping
    public List<OrderResponseDto> getAll() {
        return service.getAll().stream()
                .map(order -> converter.toDto(order))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderResponseDto getById(@PathVariable Long id) {
        return converter.toDto(service.getById(id));
    }

}
