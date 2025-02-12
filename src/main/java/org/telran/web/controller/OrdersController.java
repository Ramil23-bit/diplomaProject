package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

/**
 * Controller for managing orders.
 * Provides endpoints for creating, retrieving, and listing orders.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    @Autowired
    private OrdersService service;

    @Autowired
    private OrderCreateConverter converter;

    @Autowired
    private OrderItemsConverter itemsConverter;

    /**
     * Creates a new order.
     *
     * @param dto OrderCreateDto containing the order details.
     * @return ResponseEntity with the created order details.
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderCreateDto dto) {
        Orders order = service.create(converter.toEntity(dto));
        List<OrderItemsCreateDto> orderItemsDto = Optional.ofNullable(dto.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .peek(a -> a.setOrderId(order.getId()))
                .collect(Collectors.toList());
        List<OrderItems> orderItems = orderItemsDto.stream().map(itemsConverter::toEntity).collect(Collectors.toList());
        orderItems.forEach(oI -> oI.setPriceAtPurchase(oI.getProduct().getPrice()));
        order.setOrderItems(orderItems);
        OrderResponseDto responseDto = converter.toDto(service.create(order));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Retrieves all orders.
     *
     * @return List of OrderResponseDto representing all orders.
     */
    @GetMapping
    public List<OrderResponseDto> getAll() {
        return service.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id ID of the order.
     * @return OrderResponseDto representing the found order.
     */
    @GetMapping("/{id}")
    public OrderResponseDto getById(@PathVariable Long id) {
        return converter.toDto(service.getById(id));
    }
}
