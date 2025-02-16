package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.CartItemsCreateDto;
import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.CartItems;
import org.telran.web.service.CartService;
import org.telran.web.service.ProductService;

/**
 * Converter class for transforming CartItems entities to DTOs and vice versa.
 * Handles the conversion between CartItems, CartItemsCreateDto, and CartItemsResponseDto.
 */
@Component
public class CartItemsConverter implements Converter<CartItems, CartItemsCreateDto, CartItemsResponseDto> {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCreateConverter productCreateConverter;

    @Autowired
    private UserCreateConverter userCreateConverter;

    /**
     * Converts a CartItems entity to a CartItemsResponseDto.
     *
     * @param cartItems The CartItems entity to convert.
     * @return A CartItemsResponseDto representing the cart item.
     */
    @Override
    public CartItemsResponseDto toDto(CartItems cartItems) {
        return new CartItemsResponseDto(cartItems.getId(), cartItems.getQuantity(),
                productCreateConverter.toDto(cartItems.getProduct()),
                userCreateConverter.toDto(cartItems.getCart().getUser()));
    }

    /**
     * Converts a CartItemsCreateDto to a CartItems entity.
     *
     * @param cartItemsCreateDto The DTO containing cart item creation data.
     * @return The created CartItems entity.
     */
    @Override
    public CartItems toEntity(CartItemsCreateDto cartItemsCreateDto) {
        return new CartItems(cartItemsCreateDto.getQuantity(),
                cartService.getByIdCart(cartItemsCreateDto.getCartId()),
                productService.getById(cartItemsCreateDto.getProductId()));
    }
}