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
import org.springframework.web.server.ResponseStatusException;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CartItemsCreateDto;
import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.CartItems;
import org.telran.web.service.CartItemsService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing cart items.
 * Provides endpoints to add, retrieve, and remove cart items.
 */
@RestController
@RequestMapping("/api/v1/cart_items")
public class CartItemsController {

    private static final Logger logger = LoggerFactory.getLogger(CartItemsController.class);

    @Autowired
    private CartItemsService cartItemsService;

    @Autowired
    private Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> cartItemsConverter;

    /**
     * Adds an item to the cart.
     *
     * @param cartItemsCreateDto The DTO containing cart item details.
     * @return The created cart item response DTO.
     */
    @Operation(summary = "Add an item to the cart", description = "Adds an item to the user's shopping cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cart item successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CartItemsResponseDto create(@RequestBody CartItemsCreateDto cartItemsCreateDto) {

        logger.info("Received request to add item to cart: {}", cartItemsCreateDto);

        CartItems cartItems = cartItemsConverter.toEntity(cartItemsCreateDto);
        logger.info("Creating CartItem entity: {}", cartItems);

        CartItems savedCartItem = cartItemsService.createCartItems(cartItems);

        CartItemsResponseDto response = cartItemsConverter.toDto(savedCartItem);
        logger.info("Converted CartItem DTO: {}", response);

        logger.info("Cart item added successfully with ID: {}", response.getId());

        return response;
    }


    /**
     * Retrieves all cart items (Admin only).
     *
     * @return List of cart items response DTOs.
     */
    @Operation(summary = "Get all cart items", description = "Retrieves a list of all cart items. Accessible by ADMIN users only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart items successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CartItemsResponseDto> getAll() {
        logger.info("Admin fetching all cart items");
        List<CartItemsResponseDto> cartItems = cartItemsService.getAllCartItems().stream()
                .map(cartItemsConverter::toDto)
                .collect(Collectors.toList());
        logger.info("Total cart items retrieved: {}", cartItems.size());
        return cartItems;
    }

    /**
     * Retrieves the cart items of the current user.
     *
     * @return List of cart items response DTOs.
     */
    @Operation(summary = "Get current user's cart items", description = "Retrieves all items in the logged-in user's cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart items successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CartItemsResponseDto> getCurrent() {
        logger.info("Fetching current user's cart items");
        List<CartItemsResponseDto> cartItems = cartItemsService.getAllByCurrentUser().stream()
                .map(cartItemsConverter::toDto)
                .collect(Collectors.toList());
        logger.info("Total cart items retrieved for current user: {}", cartItems.size());
        return cartItems;
    }

    /**
     * Deletes a cart item by its ID.
     *
     * @param id The ID of the cart item to delete.
     */
    @Operation(summary = "Delete cart item by ID", description = "Removes a specific item from the cart by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart item successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable(name = "id") Long id) {
        logger.info("Request to delete cart item with ID: {}", id);
        cartItemsService.deleteById(id);
        logger.info("Cart item with ID {} successfully deleted", id);
    }
}
