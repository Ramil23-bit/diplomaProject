package org.telran.web.service;

import org.telran.web.entity.Cart;

import java.util.List;

public interface CartService {

    Cart createCart(Cart cart);
    void deleteByUser(Long userId);
    Cart getByIdCart(Long id);
    List<Cart> getAllCart();
    Cart findByCurrentUser();
}
