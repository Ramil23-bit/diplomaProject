package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartResponseDto> create(@RequestBody CartCreateDto cartCreateDto) {
        Cart cart = cartConverter.toEntity(cartCreateDto);
        Cart savedCart = cartService.createCart(cart);
        CartResponseDto response = cartConverter.toDto(savedCart);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CartResponseDto getCurrentCart(){
        return cartConverter.toDto(cartService.findByCurrentUser());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CartResponseDto> getCarts(){
        System.out.println("🔐 Проверка роли в getCarts(): " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return cartService.getAllCart().stream()
                .map(cart -> cartConverter.toDto(cart))
                .collect(Collectors.toList());
    }
}
