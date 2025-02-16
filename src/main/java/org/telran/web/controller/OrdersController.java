package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponseDto> getAll() {
        logger.info("Fetching all orders");
        List<OrderResponseDto> orders = service.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        logger.info("Total orders retrieved: {}", orders.size());
        return orders;
    }

    /**
     * Retrieves the order history for the currently authenticated user.
     *
     * This method handles HTTP GET requests to the "/history" endpoint.
     * It fetches all orders associated with the user's ID from the service layer,
     * converts each order to a Data Transfer Object (DTO) using the converter,
     * and returns a list of order response DTOs.
     *
     * @return a list of {@link OrderResponseDto} representing the order history
     *         of the authenticated user. If there are no orders, an empty list is returned.
     *
     * @throws "UnauthorizedException" if the user is not authenticated or does not have permission
     *         to access the order history.
     *
     * @see OrderResponseDto
     */
    @Operation(summary = "Get History", description = "Gets order history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order history received successfully")
    })
    @GetMapping("/history")
    public List<OrderResponseDto> getHistory() {
        return service.getAllByUserIdHistory().stream()
                .map(orders -> converter.toDto(orders))
                .collect(Collectors.toList());
    }

    /**
     * Checks the status of orders for the currently authenticated user.
     *
     * This method handles HTTP GET requests to the "/status" endpoint.
     * It retrieves the current status of all orders associated with the user's ID
     * from the service layer, converts each order to a Data Transfer Object (DTO)
     * using the converter, and returns a list of order response DTOs.
     *
     * @return a list of {@link OrderResponseDto} representing the current status
     *         of the orders for the authenticated user. If there are no orders,
     *         an empty list is returned.
     *
     * @throws "UnauthorizedException" if the user is not authenticated or does not have permission
     *         to access the order status.
     *
     * @see OrderResponseDto
     */
    @Operation(summary = "Get Status Order", description = "Gets order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status received successfully")
    })
    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<OrderResponseDto> checkStatus() {
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
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponseDto getById(@PathVariable Long id) {
        logger.info("Fetching order with ID: {}", id);
        OrderResponseDto order = converter.toDto(service.getById(id));
        logger.info("Order retrieved: {}", order);
        return order;
    }
}
