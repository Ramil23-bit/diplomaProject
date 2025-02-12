package org.telran.web.controller;

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
 * Provides endpoints for creating, retrieving, and listing carts.
 */
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private Converter<Cart, CartCreateDto, CartResponseDto> cartConverter;

    /**
     * Creates a new shopping cart.
     *
     * @param cartCreateDto DTO containing the cart details.
     * @return ResponseEntity with the created cart details.
     */
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
     * Retrieves the current user's active cart.
     *
     * @return CartResponseDto representing the current user's cart.
     */
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CartResponseDto getCurrentCart() {
        return cartConverter.toDto(cartService.findByCurrentUser());
    }

    /**
     * Retrieves all shopping carts. Available only for admins.
     *
     * @return List of CartResponseDto representing all carts.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CartResponseDto> getCarts() {
        return cartService.getAllCart().stream()
                .map(cartConverter::toDto)
                .collect(Collectors.toList());
    }
}
