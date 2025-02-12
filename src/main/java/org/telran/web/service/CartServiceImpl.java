package org.telran.web.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

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
        logger.info("Creating a new cart for user ID: {}", cart.getUser().getId());
        Cart savedCart = cartJpaRepository.save(cart);
        logger.info("Cart created successfully with ID: {}", savedCart.getId());
        return savedCart;
    }

    /**
     * Adds an item to the current user's cart.
     *
     * @param cart Cart entity containing the item to add.
     * @return The updated Cart entity.
     */
    @Override
    public Cart addItemToCart(Cart cart) {
        logger.info("Adding item to the current user's cart");
        Cart currentCart = findByCurrentUser();
        CartItems cartItem = cart.getCartItemsList().get(0);
        currentCart.getCartItemsList().add(cartItem);
        logger.info("Item added to cart successfully, cart ID: {}", currentCart.getId());
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
        logger.info("Fetching cart with ID: {}", id);
        return cartJpaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cart with ID {} not found", id);
                    return new CartNotFoundException("Cart by " + id + " not Found");
                });
    }

    /**
     * Deletes a cart by user ID.
     *
     * @param userId ID of the user whose cart should be deleted.
     */
    @Override
    public void deleteByUser(Long userId) {
        logger.info("Deleting cart for user ID: {}", userId);
        cartJpaRepository.deleteByUser(userId);
        logger.info("Cart for user ID {} deleted successfully", userId);
    }

    /**
     * Retrieves all carts from the repository.
     *
     * @return List of all Cart entities.
     */
    @Transactional
    @Override
    public List<Cart> getAllCart() {
        logger.info("Fetching all carts");
        List<Cart> carts = cartJpaRepository.findAll();
        logger.info("Total carts retrieved: {}", carts.size());
        return carts;
    }

    /**
     * Retrieves the cart associated with the currently logged-in user.
     *
     * @return The Cart entity linked to the current user.
     * @throws CartNotFoundException if no cart is found for the current user.
     */
    @Override
    public Cart findByCurrentUser() {
        logger.info("Fetching cart for current user ID: {}", userService.getCurrentUserId());
        return cartJpaRepository.findByUserId(userService.getCurrentUserId())
                .orElseThrow(() -> {
                    logger.error("Cart for current user not found");
                    return new CartNotFoundException("Cart not Found");
                });
    }
}
