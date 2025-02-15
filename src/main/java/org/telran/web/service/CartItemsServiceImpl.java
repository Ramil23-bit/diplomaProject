package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.dto.CartItemsResponseDto;
import org.telran.web.entity.Cart;
import org.telran.web.entity.CartItems;
import org.telran.web.exception.CartItemsNotFoundException;
import org.telran.web.repository.CartItemsJpaRepository;

import java.util.List;

/**
 * Implementation of CartItemsService.
 * Handles business logic for managing cart items.
 */
@Service
public class CartItemsServiceImpl implements CartItemsService {

    private static final Logger logger = LoggerFactory.getLogger(CartItemsServiceImpl.class);

    @Autowired
    private CartItemsJpaRepository cartItemsJpaRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    /**
     * Retrieves all cart items from the repository.
     *
     * @return List of CartItems representing all cart items.
     */
    @Override
    public List<CartItems> getAllCartItems() {
        logger.info("Fetching all cart items");
        List<CartItems> cartItemsList = cartItemsJpaRepository.findAll();
        logger.info("Total cart items retrieved: {}", cartItemsList.size());
        return cartItemsList;
    }

    /**
     * Retrieves a cart item by its ID.
     *
     * @param id ID of the cart item.
     * @return CartItems representing the found cart item.
     * @throws CartItemsNotFoundException if the cart item is not found.
     */
    @Override
    public CartItems getByIdCartItems(Long id) {
        logger.info("Fetching cart item with ID: {}", id);
        return cartItemsJpaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cart item with ID {} not found", id);
                    return new CartItemsNotFoundException("Cart Items by " + id + " not Found");
                });
    }

    /**
     * Creates a new cart item and saves it in the repository.
     *
     * @param cartItems CartItems entity containing the cart item details.
     * @return CartItems representing the created cart item.
     */
    @Override
    public CartItems createCartItems(CartItems cartItems) {
        logger.info("Creating new cart item for cart ID: {}", cartItems.getCart().getId());

        if (cartItems == null) {
            logger.error("CartItems entity cannot be null");
            throw new IllegalArgumentException("Cart items cannot be null");
        }

        if (cartItems.getCart() == null || cartItems.getProduct() == null) {
            logger.error("Cart or Product cannot be null for cart item: {}", cartItems);
            throw new IllegalArgumentException("Cart and Product must not be null");
        }

        CartItems savedCartItem = cartItemsJpaRepository.save(cartItems);

        if (savedCartItem == null) {
            logger.error("Failed to save cart item");
            throw new RuntimeException("Failed to create CartItem");
        }

        logger.info("Cart item created successfully with ID: {}", savedCartItem.getId());
        return savedCartItem;
    }

    /**
     * Retrieves all cart items associated with the current user.
     *
     * @return List of CartItems representing the current user's cart items.
     */
    @Override
    public List<CartItems> getAllByCurrentUser() {
        Long currentUserCartId = cartService.findByCurrentUser().getId();
        logger.info("Fetching all cart items for current user, cart ID: {}", currentUserCartId);
        List<CartItems> cartItemsList = cartItemsJpaRepository.findAllByCartId(currentUserCartId);
        logger.info("Total cart items for current user: {}", cartItemsList.size());
        return cartItemsList;
    }

    /**
     * Deletes a cart item by its ID.
     *
     * @param CartItemsId ID of the cart item to delete.
     */
    @Override
    public void deleteById(Long CartItemsId) {
        logger.info("Deleting cart item with ID: {}", CartItemsId);
        if (!cartItemsJpaRepository.existsById(CartItemsId)) {
            logger.error("Cart item with ID {} not found, cannot delete", CartItemsId);
            throw new CartItemsNotFoundException("Cart Items by " + CartItemsId + " not Found");
        }
        cartItemsJpaRepository.deleteById(CartItemsId);
        logger.info("Cart item with ID {} successfully deleted", CartItemsId);
    }

    @Override
    public void removeCartItem(CartItems cartItems) {
        cartItemsJpaRepository.delete(cartItems);
    }
}
