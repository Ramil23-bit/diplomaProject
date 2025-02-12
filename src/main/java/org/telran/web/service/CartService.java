package org.telran.web.service;

import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.entity.CartItems;

import java.util.List;

/**
 * Service interface for managing shopping carts.
 * Provides methods for cart creation, retrieval, modification, and deletion.
 */
public interface CartService {

    /**
     * Creates a new cart.
     *
     * @param cart Cart entity containing cart details.
     * @return The created Cart entity.
     */
    Cart createCart(Cart cart);

    /**
     * Deletes a cart by user ID.
     *
     * @param userId ID of the user whose cart should be deleted.
     */
    void deleteByUser(Long userId);

    /**
     * Retrieves a cart by its ID.
     *
     * @param id ID of the cart.
     * @return The found Cart entity.
     */
    Cart getByIdCart(Long id);

    /**
     * Retrieves all carts.
     *
     * @return List of all Cart entities.
     */
    List<Cart> getAllCart();

    /**
     * Retrieves the cart associated with the currently logged-in user.
     *
     * @return The Cart entity linked to the current user.
     */
    Cart findByCurrentUser();

    /**
     * Adds an item to an existing cart.
     *
     * @param cart Cart entity to which the item should be added.
     * @return The updated Cart entity.
     */
    Cart addItemToCart(Cart cart);
}