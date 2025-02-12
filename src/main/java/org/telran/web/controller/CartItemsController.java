package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CartItemsCreateDto;
import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.CartItems;
import org.telran.web.service.CartItemsService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing cart items.
 * Provides endpoints to add, retrieve, and delete items in a cart.
 */
@RestController
@RequestMapping("/api/v1/cart_items")
public class CartItemsController {

    @Autowired
    private CartItemsService cartItemsService;

    @Autowired
    private Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> cartItemsConverter;

    /**
     * Adds a new item to the shopping cart.
     *
     * @param cartItemsCreateDto The DTO containing cart item details.
     * @return The created cart item response DTO.
     */
    @Operation(summary = "Add item to cart", description = "Adds an item to the user's shopping cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cart item successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CartItemsResponseDto create(@RequestBody CartItemsCreateDto cartItemsCreateDto) {
        return cartItemsConverter.toDto(cartItemsService.createCartItems(cartItemsConverter.toEntity(cartItemsCreateDto)));
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
        return cartItemsService.getAllCartItems().stream()
                .map(cartItemsConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a cart item by its ID.
     *
     * @param id The ID of the cart item.
     * @return The cart item response DTO.
     */
    @Operation(summary = "Get cart item by ID", description = "Retrieves details of a specific cart item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CartItemsResponseDto getById(@PathVariable(name = "id") Long id) {
        return cartItemsConverter.toDto(cartItemsService.getByIdCartItems(id));
    }

    /**
     * Retrieves the current user's cart items.
     *
     * @return List of cart items response DTOs.
     */
    @Operation(summary = "Get current user's cart items", description = "Retrieves all cart items for the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart items successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CartItemsResponseDto> getCurrent() {
        return cartItemsService.getAllByCurrentUser().stream()
                .map(cartItemsConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a cart item by its ID.
     *
     * @param id The ID of the cart item to delete.
     */
    @Operation(summary = "Delete cart item by ID", description = "Removes a specific cart item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable(name = "id") Long id) {
        cartItemsService.deleteById(id);
    }
}
