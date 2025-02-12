package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.CartItemsCreateDto;
import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.CartItems;
import org.telran.web.entity.User;
import org.telran.web.service.CartService;
import org.telran.web.service.ProductService;

@Component
public class CartItemsConverter implements Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;
    @Autowired
    private Converter<User, ?, UserResponseDto> userConverter;
    @Override
    public CartItemsResponseDto toDto(CartItems cartItems) {
        UserResponseDto userResponseDto = userConverter.toDto(cartItems.getCart().getUser());
        return new CartItemsResponseDto(cartItems.getId(), cartItems.getQuantity(), cartItems.getProduct(), userResponseDto);
    }

    @Override
    public CartItems toEntity(CartItemsCreateDto cartItemsCreateDto) {
        return new CartItems(cartItemsCreateDto.getQuantity(), cartService.getByIdCart(cartItemsCreateDto.getCartId()), productService.getById(cartItemsCreateDto.getProductId()));
    }
}
