package org.telran.web.service;

import org.telran.web.entity.CartItems;

import java.util.List;

public interface CartItemsService {

    List<CartItems> getAllCartItems();

    CartItems getByIdCartItems(Long id);

    CartItems createCartItems(CartItems cartItems);


}
