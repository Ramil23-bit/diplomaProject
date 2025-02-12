package org.telran.web.service;

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
        return cartItemsJpaRepository.findAll();
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
        return cartItemsJpaRepository.findById(id)
                .orElseThrow(() -> new CartItemsNotFoundException("Cart Items by " + id + " not Found"));
    }

    /**
     * Creates a new cart item and saves it in the repository.
     *
     * @param cartItems CartItems entity containing the cart item details.
     * @return CartItems representing the created cart item.
     */
    @Override
    public CartItems createCartItems(CartItems cartItems) {
        return cartItemsJpaRepository.save(cartItems);
    }

    /**
     * Retrieves all cart items associated with the current user.
     *
     * @return List of CartItems representing the current user's cart items.
     */
    @Override
    public List<CartItems> getAllByCurrentUser() {
        Long currentUserCartId = cartService.findByCurrentUser().getId();
        return cartItemsJpaRepository.findAllByCartId(currentUserCartId);
    }

    /**
     * Deletes a cart item by its ID.
     *
     * @param CartItemsId ID of the cart item to delete.
     */
    @Override
    public void deleteById(Long CartItemsId) {
        cartItemsJpaRepository.deleteById(CartItemsId);
    }
}
