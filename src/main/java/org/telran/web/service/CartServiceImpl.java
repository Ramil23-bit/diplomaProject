package org.telran.web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.entity.CartItems;
import org.telran.web.entity.User;
import org.telran.web.exception.CartNotFoundException;
import org.telran.web.repository.CartJpaRepository;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartJpaRepository cartJpaRepository;

    @Lazy
    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private CartItemsService cartItemsService;

    @Override
    public Cart createCart(Cart cart) {
        return cartJpaRepository.save(cart);
    }

    @Override
    public Cart addItemToCart(Cart cart) {
        Cart save = cartJpaRepository.save(cart);
//        cartItemsService.createCartItems(cart.getCartItemsList())
        return save;
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

    @Transactional
    @Override
    public List<Cart> getAllCart() {
        return cartJpaRepository.findAll();
    }

    @Override
    public Cart findByCurrentUser() {
        return cartJpaRepository.findByUserId(userService.getCurrentUserId())
                .orElseThrow(() -> new CartNotFoundException("Cart not Found"));
    }
}
