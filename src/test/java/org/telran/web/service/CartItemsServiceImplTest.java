package org.telran.web.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Cart;
import org.telran.web.entity.CartItems;
import org.telran.web.entity.Product;
import org.telran.web.exception.CartItemsNotFoundException;
import org.telran.web.repository.CartItemsJpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Key Features:
 * - Uses `@ExtendWith(MockitoExtension.class)` to integrate Mockito.
 * - Mocks `CartItemsJpaRepository` to isolate service layer behavior.
 * - Tests for **valid and invalid retrieval of cart items**.
 * - Ensures **correct creation of cart items**.
 * - Validates **exception handling for missing cart items**.
 */

@ExtendWith(MockitoExtension.class)
class CartItemsServiceImplTest {

    @Mock
    private CartItemsJpaRepository cartItemsJpaRepository;

    @InjectMocks
    private CartItemsServiceImpl cartItemsService;

    private CartItems cartItem1;
    private CartItems cartItem2;
    private Cart cart;
    private Product product;

    /**
     * Setup Method: Initializes test data before each test**
     * - Creates a cart and product instance.
     * - Creates two sample cart items for testing.
     */
    @BeforeEach
    void setUp() {
        cart = new Cart();
        product = new Product();
        cartItem1 = new CartItems(1L, 10L, cart, product);
        cartItem2 = new CartItems(2L, 5L, cart, product);
    }

    /**
     * *Test Case:** Retrieve all cart items.
     * *Expected Result:** Returns a list of cart items.
     */
    @Test
    void testGetAllCartItems() {
        when(cartItemsJpaRepository.findAll()).thenReturn(Arrays.asList(cartItem1, cartItem2));

        List<CartItems> result = cartItemsService.getAllCartItems();

        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).getQuantity());
        verify(cartItemsJpaRepository, times(1)).findAll();
    }

    /**
     * *Test Case:** Retrieve a cart item by ID when it exists.
     * *Expected Result:** Returns the correct cart item.
     */
    @Test
    void testGetByIdCartItems_Found() {
        when(cartItemsJpaRepository.findById(1L)).thenReturn(Optional.of(cartItem1));

        CartItems result = cartItemsService.getByIdCartItems(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10L, result.getQuantity());
        verify(cartItemsJpaRepository, times(1)).findById(1L);
    }

    /**
     * *Test Case:** Retrieve a cart item by ID when it does not exist.
     * *Expected Result:** Throws `CartItemsNotFoundException`.
     */
    @Test
    void testGetByIdCartItems_NotFound() {
        when(cartItemsJpaRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(CartItemsNotFoundException.class, () -> cartItemsService.getByIdCartItems(3L));
        verify(cartItemsJpaRepository, times(1)).findById(3L);
    }

    /**
     * *Test Case:** Create a new cart item.
     * *Expected Result:** Returns the created cart item.
     */
    @Test
    void testCreateCartItems() {
        when(cartItemsJpaRepository.save(any(CartItems.class))).thenReturn(cartItem1);

        CartItems result = cartItemsService.createCartItems(cartItem1);

        assertNotNull(result);
        assertEquals(10L, result.getQuantity());
        verify(cartItemsJpaRepository, times(1)).save(cartItem1);
    }
}