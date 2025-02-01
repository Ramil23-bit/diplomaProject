package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.OrderCreateConverter;
import org.telran.web.converter.OrderItemsConverter;
import org.telran.web.dto.*;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Orders;
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

    @Autowired
    private OrderItemsConverter itemsConverter;

    @PostMapping
    public OrderResponseDto create(@Valid @RequestBody OrderCreateDto dto) {
        Orders order = service.create(converter.toEntity(dto));
        List<OrderItemsCreateDto> orderItemsDto = dto.getOrderItems().stream().peek(a -> a.setOrderId(order.getId())).collect(Collectors.toList());
        List<OrderItems> orderItems = orderItemsDto.stream().map(a -> itemsConverter.toEntity(a)).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        return converter.toDto(service.create(order));
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
