package org.telran.web.converter;

import org.springframework.stereotype.Component;
import org.telran.web.dto.CartItemsCreateDto;
import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.CartItems;
@Component
public class CartItemsConverter implements Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> {
    @Override
    public CartItemsResponseDto toDto(CartItems cartItems) {
        return new CartItemsResponseDto(cartItems.getId(), cartItems.getQuantity(), cartItems.getProduct());
    }

    @Override
    public CartItems toEntity(CartItemsCreateDto cartItemsCreateDto) {
        return new CartItems(cartItemsCreateDto.getCartId(), cartItemsCreateDto.getProductId());
    }
}
