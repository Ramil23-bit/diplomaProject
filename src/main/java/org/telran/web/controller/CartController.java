package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CartCreateDto;
import org.telran.web.dto.CartResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.repository.CartJpaRepository;
import org.telran.web.service.CartService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private Converter<Cart, CartCreateDto, CartResponseDto> cartConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponseDto create(@RequestBody CartCreateDto cartCreateDto){
        return cartConverter.toDto(cartService.createCart(cartConverter.toEntity(cartCreateDto)));
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CartResponseDto getCurrentCart(){
        return cartConverter.toDto(cartService.findByCurrentUser());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CartResponseDto> getCarts(){
        return cartService.getAllCart().stream()
                .map(cart -> cartConverter.toDto(cart))
                .collect(Collectors.toList());
    }
}
