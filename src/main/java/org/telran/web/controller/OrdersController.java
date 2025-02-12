package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.OrderCreateConverter;
import org.telran.web.converter.OrderItemsConverter;
import org.telran.web.dto.*;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Orders;
import org.telran.web.service.OrdersService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN')")
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderCreateDto dto) {
        Orders order = service.create(converter.toEntity(dto));
        List<OrderItemsCreateDto> orderItemsDto = Optional.ofNullable(dto.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .peek(a -> a.setOrderId(order.getId()))
                .collect(Collectors.toList());
        List<OrderItems> orderItems = orderItemsDto.stream().map(a -> itemsConverter.toEntity(a)).collect(Collectors.toList());
        orderItems.forEach(oI -> oI.setPriceAtPurchase(oI.getProduct().getPrice()));
        order.setOrderItems(orderItems);
        OrderResponseDto responseDto = converter.toDto(service.create(order));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<OrderResponseDto> getAll() {
        return service.getAll().stream()
                .map(order -> converter.toDto(order))
                .collect(Collectors.toList());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponseDto> checkStatus(){
        return service.checkOrderStatus().stream()
                .map(orders -> converter.toDto(orders))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public OrderResponseDto getById(@PathVariable Long id) {
        return converter.toDto(service.getById(id));
    }

}
