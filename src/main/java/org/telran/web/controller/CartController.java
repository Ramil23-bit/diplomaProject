package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CartCreateDto;
import org.telran.web.dto.CartResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.service.CartService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing shopping carts.
 * Provides endpoints to create, retrieve, and list carts.
 */
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private Converter<Cart, CartCreateDto, CartResponseDto> cartConverter;

    /**
     * Creates a new cart.
     *
     * @param cartCreateDto The DTO containing cart details.
     * @return The created cart response DTO.
     */
    @Operation(summary = "Create a new cart", description = "Creates a new shopping cart for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cart successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartResponseDto> create(@RequestBody CartCreateDto cartCreateDto) {
        CartResponseDto response = cartConverter.toDto(cartService.createCart(cartConverter.toEntity(cartCreateDto)));
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Retrieves the current user's cart.
     *
     * @return The cart response DTO.
     */
    @Operation(summary = "Get current user's cart", description = "Retrieves the shopping cart of the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CartResponseDto getCurrentCart() {
        return cartConverter.toDto(cartService.findByCurrentUser());
    }

    /**
     * Retrieves all carts (Admin only).
     *
     * @return List of cart response DTOs.
     */
    @Operation(summary = "Get all carts", description = "Retrieves a list of all shopping carts. Accessible by ADMIN users only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carts successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CartResponseDto> getCarts() {
        return cartService.getAllCart().stream()
                .map(cartConverter::toDto)
                .collect(Collectors.toList());
    }
}
