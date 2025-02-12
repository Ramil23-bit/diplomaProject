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

/**
 * Implementation of CartService.
 * Handles business logic for managing shopping carts.
 */
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

    /**
     * Creates a new cart and saves it in the repository.
     *
     * @param cart Cart entity containing cart details.
     * @return The created Cart entity.
     */
    @Override
    public Cart createCart(Cart cart) {
        return cartJpaRepository.save(cart);
    }

    /**
     * Adds an item to the current user's cart.
     *
     * @param cart Cart entity containing the item to add.
     * @return The updated Cart entity.
     */
    @Override
    public Cart addItemToCart(Cart cart) {
        Cart currentCart = findByCurrentUser();
        CartItems cartItem = cart.getCartItemsList().get(0);
        currentCart.getCartItemsList().add(cartItem);
        return currentCart;
    }

    /**
     * Retrieves a cart by its ID.
     *
     * @param id ID of the cart.
     * @return The found Cart entity.
     * @throws CartNotFoundException if the cart is not found.
     */
    @Override
    public Cart getByIdCart(Long id) {
        return cartJpaRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart by " + id + " not Found"));
    }

    /**
     * Deletes a cart by user ID.
     *
     * @param userId ID of the user whose cart should be deleted.
     */
    @Override
    public void deleteByUser(Long userId) {
        cartJpaRepository.deleteByUser(userId);
    }

    /**
     * Retrieves all carts from the repository.
     *
     * @return List of all Cart entities.
     */
    @Transactional
    @Override
    public List<Cart> getAllCart() {
        return cartJpaRepository.findAll();
    }

    /**
     * Retrieves the cart associated with the currently logged-in user.
     *
     * @return The Cart entity linked to the current user.
     * @throws CartNotFoundException if no cart is found for the current user.
     */
    @Override
    public Cart findByCurrentUser() {
        return cartJpaRepository.findByUserId(userService.getCurrentUserId())
                .orElseThrow(() -> new CartNotFoundException("Cart not Found"));
    }
}
