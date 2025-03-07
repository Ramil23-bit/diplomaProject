package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private static final Logger logger = LoggerFactory.getLogger(OrderItemsController.class);

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
    @Operation(summary = "Create a new order item", description = "Adds a new item to an order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItemsResponseDto create(@RequestBody OrderItemsCreateDto orderItemsCreateDto) {
        logger.info("Received request to create order item: {}", orderItemsCreateDto);
        OrderItemsResponseDto response = orderItemsConverter.toDto(
                orderItemsService.createOrderItems(orderItemsConverter.toEntity(orderItemsCreateDto)));
        logger.info("Order item created successfully with ID: {}", response.getId());
        return response;
    }

    /**
     * Retrieves all order items.
     *
     * @return List of order item response DTOs.
     */
    @Operation(summary = "Get all order items", description = "Retrieves a list of all order items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items successfully retrieved")
    })
    @GetMapping
    public List<OrderItemsResponseDto> getAll() {
        logger.info("Fetching all order items");
        List<OrderItemsResponseDto> orderItems = orderItemsService.getAllOrderItems().stream()
                .map(orderItemsConverter::toDto)
                .collect(Collectors.toList());
        logger.info("Total order items retrieved: {}", orderItems.size());
        return orderItems;
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
        logger.info("Fetching order item with ID: {}", id);
        OrderItemsResponseDto orderItem = orderItemsConverter.toDto(orderItemsService.getByIdOrderItems(id));
        logger.info("Order item retrieved: {}", orderItem);
        return orderItem;
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        logger.info("Request to delete order item with ID: {}", id);
        orderItemsService.deleteOrderItems(id);
        logger.info("Order item with ID {} successfully deleted", id);
    }


}
