package org.telran.web.controller;

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
 * Provides endpoints for creating, retrieving, listing, and deleting cart items.
 */
@RestController
@RequestMapping("/api/v1/cart_items")
public class CartItemsController {

    @Autowired
    private CartItemsService cartItemsService;

    @Autowired
    private Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> cartItemsConverter;

    /**
     * Creates a new cart item.
     *
     * @param cartItemsCreateDto DTO containing the cart item details.
     * @return CartItemsResponseDto representing the created cart item.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CartItemsResponseDto create(@RequestBody CartItemsCreateDto cartItemsCreateDto){
        return cartItemsConverter.toDto(cartItemsService.createCartItems(cartItemsConverter.toEntity(cartItemsCreateDto)));
    }

    /**
     * Retrieves all cart items. Available only for admins.
     *
     * @return List of CartItemsResponseDto representing all cart items.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CartItemsResponseDto> getAll(){
        return cartItemsService.getAllCartItems().stream()
                .map(cartItemsConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific cart item by its ID. Available only for admins.
     *
     * @param id ID of the cart item.
     * @return CartItemsResponseDto representing the found cart item.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CartItemsResponseDto getById(@PathVariable(name = "id") Long id){
        return cartItemsConverter.toDto(cartItemsService.getByIdCartItems(id));
    }

    /**
     * Retrieves all cart items belonging to the current user.
     *
     * @return List of CartItemsResponseDto representing the current user's cart items.
     */
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CartItemsResponseDto> getCurrent(){
        return cartItemsService.getAllByCurrentUser().stream()
                .map(cartItemsConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a cart item by its ID.
     *
     * @param id ID of the cart item to delete.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable(name = "id") Long id){
        cartItemsService.deleteById(id);
    }
}
