package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Cart;
import org.telran.web.exception.CartNotFoundException;
import org.telran.web.repository.CartJpaRepository;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartJpaRepository cartJpaRepository;

    @Override
    public Cart createCart(Cart cart) {
        return cartJpaRepository.save(cart);
    }

    @Override
    public Cart getByIdCart(Long id) {
        return cartJpaRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart by " + id + " not Found"));
    }

    @Override
    public void deleteByUser(Long userId) {
        cartJpaRepository.deleteByUser(userId);
    }
}
