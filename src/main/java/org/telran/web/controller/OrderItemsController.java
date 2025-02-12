package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.OrderItemsCreateDto;
import org.telran.web.dto.OrderItemsResponseDto;
import org.telran.web.entity.OrderItems;
import org.telran.web.service.OrderItemsService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing order items.
 * Provides endpoints for creating, retrieving, and deleting order items.
 */
@RestController
@RequestMapping("/api/v1/order_items")
public class OrderItemsController {

    @Autowired
    private OrderItemsService orderItemsService;

    @Autowired
    private Converter<OrderItems, OrderItemsCreateDto, OrderItemsResponseDto> orderItemsConverter;

    /**
     * Creates a new order item.
     *
     * @param orderItemsCreateDto DTO containing the order item details.
     * @return OrderItemsResponseDto representing the created order item.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItemsResponseDto create(@RequestBody OrderItemsCreateDto orderItemsCreateDto){
        return orderItemsConverter.toDto(
                orderItemsService.createOrderItems(
                        orderItemsConverter.toEntity(orderItemsCreateDto)));
    }

    /**
     * Retrieves all order items.
     *
     * @return List of OrderItemsResponseDto representing all order items.
     */
    @GetMapping
    public List<OrderItemsResponseDto> getAll(){
        return orderItemsService.getAllOrderItems().stream()
                .map(orderItemsConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an order item by its ID.
     *
     * @param id ID of the order item.
     * @return OrderItemsResponseDto representing the found order item.
     */
    @GetMapping("/{id}")
    public OrderItemsResponseDto getById(@PathVariable(name = "id") Long id){
        return orderItemsConverter.toDto(orderItemsService.getByIdOrderItems(id));
    }

    /**
     * Deletes an order item by its ID.
     *
     * @param id ID of the order item to delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        orderItemsService.deleteOrderItems(id);
    }
}
