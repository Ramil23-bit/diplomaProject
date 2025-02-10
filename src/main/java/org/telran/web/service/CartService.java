package org.telran.web.service;

import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.entity.CartItems;

import java.util.List;

public interface CartService {

    Cart createCart(Cart cart);
    void deleteByUser(Long userId);
    Cart getByIdCart(Long id);
    List<Cart> getAllCart();
    Cart findByCurrentUser();
    Cart addItemToCart(Cart cart);
}
