package org.telran.web.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Cart;
import org.telran.web.entity.User;
import org.telran.web.exception.CartNotFoundException;
import org.telran.web.repository.CartJpaRepository;
import java.util.Optional;

/**

 *   Key Features:
 * - Uses `@ExtendWith(MockitoExtension.class)` for Mockito integration.
 * - Mocks `CartJpaRepository` to isolate service layer behavior.
 * - Tests for **valid and invalid cart retrieval**.
 * - Ensures **proper cart creation**.
 * - Validates **exception handling when a cart is not found**.
 */

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartJpaRepository cartJpaRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private User user;

    /**
     *   Setup Method: Initializes test data before each test.
     * - Creates a user instance.
     * - Creates a cart associated with the user.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        cart = new Cart(1L, user);
    }

    /**
     **Test Case:** Successfully create a new cart.
     **Expected Result:** Returns the created cart.
     */
    @Test
    void testCreateCart() {
        when(cartJpaRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.createCart(cart);

        assertNotNull(result);  // The created cart must not be null
        assertEquals(1L, result.getId());  // The cart ID must match expected value
        verify(cartJpaRepository, times(1)).save(cart);  // Ensure repository save is called once
    }

    /**
     **Test Case:** Retrieve a cart by ID when it exists.
     **Expected Result:** Returns the correct cart.
     */
    @Test
    void testGetByIdCart_Found() {
        when(cartJpaRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.getByIdCart(1L);

        assertNotNull(result);  // The retrieved cart must not be null
        assertEquals(1L, result.getId());  // The cart ID must match expected value
        verify(cartJpaRepository, times(1)).findById(1L);  // Ensure repository method is called once
    }

    /**
     **Test Case:** Retrieve a cart by ID when it does not exist.
     **Expected Result:** Throws `CartNotFoundException`.
     */
    @Test
    void testGetByIdCart_NotFound() {
        when(cartJpaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.getByIdCart(2L));
        verify(cartJpaRepository, times(1)).findById(2L);  // Ensure repository method is called once
    }
}