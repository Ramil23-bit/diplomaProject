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

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartJpaRepository cartJpaRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        cart = new Cart(1L, user);
    }

    @Test
    void testCreateCart() {
        when(cartJpaRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.createCart(cart);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartJpaRepository, times(1)).save(cart);
    }

    @Test
    void testGetByIdCart_Found() {
        when(cartJpaRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart result = cartService.getByIdCart(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartJpaRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdCart_NotFound() {
        when(cartJpaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.getByIdCart(2L));
        verify(cartJpaRepository, times(1)).findById(2L);
    }
}
