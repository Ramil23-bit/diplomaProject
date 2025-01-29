package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.CartItems;
import org.telran.web.exception.CartItemsNotFoundException;
import org.telran.web.repository.CartItemsJpaRepository;

import java.util.List;

@Service
public class CartItemsServiceImpl implements CartItemsService {

    @Autowired
    private CartItemsJpaRepository cartItemsJpaRepository;
    @Override
    public List<CartItems> getAllCartItems() {
        return cartItemsJpaRepository.findAll();
    }

    @Override
    public CartItems getByIdCartItems(Long id) {
        return cartItemsJpaRepository.findById(id)
                .orElseThrow(()-> new CartItemsNotFoundException("Cart Items by " + id + " not Found"));
    }

    @Override
    public CartItems createCartItems(CartItems cartItems) {
        return cartItemsJpaRepository.save(cartItems);
    }
}
