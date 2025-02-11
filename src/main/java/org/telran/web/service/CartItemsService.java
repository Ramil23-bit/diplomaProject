package org.telran.web.service;

import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.CartItems;

import java.util.List;

public interface CartItemsService {

    List<CartItems> getAllCartItems();

    CartItems getByIdCartItems(Long id);

    CartItems createCartItems(CartItems cartItems);

    List<CartItems> getAllByCurrentUser();

    void deleteById(Long CartItemsId);
}
