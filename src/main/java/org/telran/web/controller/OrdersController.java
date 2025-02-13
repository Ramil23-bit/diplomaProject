package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.OrderCreateConverter;
import org.telran.web.dto.OrderCreateDto;
import org.telran.web.dto.OrderResponseDto;
import org.telran.web.entity.Orders;
import org.telran.web.service.OrdersService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing orders.
 * Provides endpoints to create, retrieve, and list orders.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrdersService service;

    @Autowired
    private OrderCreateConverter converter;

    /**
     * Creates a new order.
     *
     * @param dto The DTO containing order details.
     * @return The created order response DTO.
     */
    @Operation(summary = "Create a new order", description = "Places a new order for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN')")
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderCreateDto dto) {
    public ResponseEntity<OrderResponseDto> create(@RequestBody OrderCreateDto dto) {
        logger.info("Received request to create order: {}", dto);
        Orders order = service.create(converter.toEntity(dto));
        OrderResponseDto responseDto = converter.toDto(order);
        logger.info("Order created successfully with ID: {}", responseDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Retrieves all orders.
     *
     * @return List of order response DTOs.
     */
    @Operation(summary = "Get all orders", description = "Retrieves a list of all orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders successfully retrieved")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<OrderResponseDto> getAll() {
        logger.info("Fetching all orders");
        List<OrderResponseDto> orders = service.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        logger.info("Total orders retrieved: {}", orders.size());
        return orders;
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponseDto> checkStatus(){
        return service.checkOrderStatus().stream()
                .map(orders -> converter.toDto(orders))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id The ID of the order.
     * @return The order response DTO.
     */
    @Operation(summary = "Get order by ID", description = "Retrieves details of a specific order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public OrderResponseDto getById(@PathVariable Long id) {
        logger.info("Fetching order with ID: {}", id);
        OrderResponseDto order = converter.toDto(service.getById(id));
        logger.info("Order retrieved: {}", order);
        return order;
    }

}
