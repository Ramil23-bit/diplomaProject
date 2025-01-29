package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.CartCreateDto;
import org.telran.web.dto.CartResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.service.UserService;

@Component
public class CartCreateConverter implements Converter<Cart, CartCreateDto, CartResponseDto>{

    @Autowired
    private UserService userService;
    @Override
    public CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(cart.getId(), cart.getUser());
    }

    @Override
    public Cart toEntity(CartCreateDto cartCreateDto) {
        return new Cart(cartCreateDto.getId(), userService.getById(cartCreateDto.getId()));
    }
}
