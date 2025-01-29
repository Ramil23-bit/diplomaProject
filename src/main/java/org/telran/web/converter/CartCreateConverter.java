package org.telran.web.converter;

import org.springframework.stereotype.Component;
import org.telran.web.dto.CartCreateDto;
import org.telran.web.dto.CartResponseDto;
import org.telran.web.entity.Cart;
@Component
public class CartCreateConverter implements Converter<Cart, CartCreateDto, CartResponseDto>{
    @Override
    public CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(cart.getId());
    }

    @Override
    public Cart toEntity(CartCreateDto cartCreateDto) {
        return new Cart(cartCreateDto.getId(), cartCreateDto.getUser());
    }
}
