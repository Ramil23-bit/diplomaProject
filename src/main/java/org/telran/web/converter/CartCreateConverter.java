package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.CartCreateDto;
import org.telran.web.dto.CartResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.entity.CartItems;
import org.telran.web.entity.User;
import org.telran.web.service.CartItemsService;
import org.telran.web.service.CartService;
import org.telran.web.service.ProductService;
import org.telran.web.service.UserService;

/**
 * Converter class for transforming Cart entities to DTOs and vice versa.
 * Handles the conversion between Cart, CartCreateDto, and CartResponseDto.
 */
@Component
public class CartCreateConverter implements Converter<Cart, CartCreateDto, CartResponseDto> {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserCreateConverter userCreateConverter;

    @Autowired
    private CartItemsService cartItemsService;

    /**
     * Converts a Cart entity to a CartResponseDto.
     *
     * @param cart The Cart entity to convert.
     * @return A CartResponseDto representing the cart.
     */
    @Override
    public CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(cart.getId(), userCreateConverter.toDto(cart.getUser()));
    }

    /**
     * Converts a CartCreateDto to a Cart entity, linking it to the current user and adding an item.
     *
     * @param cartCreateDto The DTO containing cart creation data.
     * @return The updated Cart entity.
     */
    @Override
    public Cart toEntity(CartCreateDto cartCreateDto) {
        User user = userService.getById(userService.getCurrentUserId());
        Cart cart = cartService.findByCurrentUser();
        Long productId = cartCreateDto.getProductId();
        CartItems cartItem = cartItemsService.createCartItems(
                new CartItems(cartCreateDto.getQuantity(), cart, productService.getById(productId)));
        cart.getCartItemsList().add(cartItem);
        return cart;
    }
}
