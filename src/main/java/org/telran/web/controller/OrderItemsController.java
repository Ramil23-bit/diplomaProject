package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * Provides endpoints to create, retrieve, and delete order items.
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
     * @param orderItemsCreateDto The DTO containing order item details.
     * @return The created order item response DTO.
     */
    @Operation(summary = "Create a new order item", description = "Adds an item to an order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItemsResponseDto create(@RequestBody OrderItemsCreateDto orderItemsCreateDto) {
        return orderItemsConverter.toDto(
                orderItemsService.createOrderItems(
                        orderItemsConverter.toEntity(orderItemsCreateDto)));
    }

    /**
     * Retrieves all order items.
     *
     * @return List of order items response DTOs.
     */
    @Operation(summary = "Get all order items", description = "Retrieves a list of all order items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items successfully retrieved")
    })
    @GetMapping
    public List<OrderItemsResponseDto> getAll() {
        return orderItemsService.getAllOrderItems().stream()
                .map(orderItemsConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an order item by its ID.
     *
     * @param id The ID of the order item.
     * @return The order item response DTO.
     */
    @Operation(summary = "Get order item by ID", description = "Retrieves details of a specific order item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @GetMapping("/{id}")
    public OrderItemsResponseDto getById(@PathVariable(name = "id") Long id) {
        return orderItemsConverter.toDto(orderItemsService.getByIdOrderItems(id));
    }

    /**
     * Deletes an order item by its ID.
     *
     * @param id The ID of the order item to delete.
     */
    @Operation(summary = "Delete order item by ID", description = "Removes a specific order item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order item successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        orderItemsService.deleteOrderItems(id);
    }
}
