package org.telran.web.service;

import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.CartItems;

import java.util.List;

/**
 * Service interface for managing cart items.
 * Provides methods for retrieving, creating, and deleting cart items.
 */
public interface CartItemsService {

    /**
     * Retrieves all cart items.
     *
     * @return List of CartItems representing all cart items.
     */
    List<CartItems> getAllCartItems();

    /**
     * Retrieves a cart item by its ID.
     *
     * @param id ID of the cart item.
     * @return CartItems representing the found cart item.
     */
    CartItems getByIdCartItems(Long id);

    /**
     * Creates a new cart item.
     *
     * @param cartItems CartItems entity containing the cart item details.
     * @return CartItems representing the created cart item.
     */
    CartItems createCartItems(CartItems cartItems);

    /**
     * Retrieves all cart items for the current user.
     *
     * @return List of CartItems representing the current user's cart items.
     */
    List<CartItems> getAllByCurrentUser();

    /**
     * Deletes a cart item by its ID.
     *
     * @param CartItemsId ID of the cart item to delete.
     */
    void deleteById(Long CartItemsId);
}
