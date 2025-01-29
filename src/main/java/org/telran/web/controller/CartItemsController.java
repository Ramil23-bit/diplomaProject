package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public CartItemsResponseDto create(@RequestBody CartItemsCreateDto cartItemsCreateDto){
        return cartItemsConverter.toDto(cartItemsService.createCartItems(cartItemsConverter.toEntity(cartItemsCreateDto)));
    }

    @GetMapping
    public List<CartItemsResponseDto> getAll(){
        return cartItemsService.getAllCartItems().stream()
                .map(cartItems -> cartItemsConverter.toDto(cartItems))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CartItemsResponseDto getById(@PathVariable(name = "id") Long id){
        return cartItemsConverter.toDto(cartItemsService.getByIdCartItems(id));
    }
}
