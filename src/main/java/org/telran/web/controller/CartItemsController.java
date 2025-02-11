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

@RestController
@RequestMapping("/api/v1/cart_items")
public class CartItemsController {

    @Autowired
    private CartItemsService cartItemsService;

    @Autowired
    private Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> cartItemsConverter;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CartItemsResponseDto create(@RequestBody CartItemsCreateDto cartItemsCreateDto){
        return cartItemsConverter.toDto(cartItemsService.createCartItems(cartItemsConverter.toEntity(cartItemsCreateDto)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CartItemsResponseDto> getAll(){
        return cartItemsService.getAllCartItems().stream()
                .map(cartItems -> cartItemsConverter.toDto(cartItems))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CartItemsResponseDto getById(@PathVariable(name = "id") Long id){
        return cartItemsConverter.toDto(cartItemsService.getByIdCartItems(id));
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CartItemsResponseDto> getCurrent(){
        return cartItemsService.getAllByCurrentUser().stream()
                .map(cartItems -> cartItemsConverter.toDto(cartItems))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable(name = "id") Long id){
        cartItemsService.deleteById(id);
    }
}
