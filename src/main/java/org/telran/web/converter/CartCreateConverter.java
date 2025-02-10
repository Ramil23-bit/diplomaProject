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

import java.util.List;

@Component
public class CartCreateConverter implements Converter<Cart, CartCreateDto, CartResponseDto>{

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

    @Override
    public CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(cart.getId(), userCreateConverter.toDto(cart.getUser()));
    }

    @Override
    public Cart toEntity(CartCreateDto cartCreateDto) {
        User user = userService.getById(userService.getCurrentUserId());
        Cart cart = cartService.findByCurrentUser();
        CartItems cartItem = cartItemsService.createCartItems(new CartItems(cartCreateDto.getQuantity(), cart, productService.getById(cartCreateDto.getProductId())));
        cart.getCartItemsList().add(cartItem);
        return cart;
    }

}
