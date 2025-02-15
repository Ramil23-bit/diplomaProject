package org.telran.web.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Orders;
import org.telran.web.entity.User;
import org.telran.web.exception.OrderNotFoundException;
import org.telran.web.repository.OrdersRepository;

/**
 *   Key Features:
 * - Uses `@ExtendWith(MockitoExtension.class)` for Mockito-based testing.
 * - Mocks `OrdersRepository` to isolate business logic.
 * - Covers scenarios like **creating an order, fetching all orders, retrieving by ID, and handling missing orders**.
 * - Ensures **exception handling for non-existing orders**.
 */

@ExtendWith(MockitoExtension.class)
class OrdersServiceImplTest {

    @Mock
    private OrdersRepository ordersRepository;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    private Orders order1;
    private Orders order2;

    /**
     *   Setup Method**
     * - Initializes test data before each test execution.
     * - Creates **two sample orders** with different delivery addresses.
     */
    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        order1 = new Orders(user, "Main Street 123", "Courier");
        order1.setId(1L);

        order2 = new Orders(user, "Royal Avenue 456", "Pickup");
        order2.setId(2L);
    }

    /**
     **Test Case:** Successfully create an order.
     **Expected Result:** The order is saved and returned correctly.
     */
    @Test
    void testCreateOrder_Success() {
        when(ordersRepository.save(order1)).thenReturn(order1);

        Orders createdOrder = ordersService.create(order1);

        assertNotNull(createdOrder, "Order must not be null");
        assertEquals(order1.getId(), createdOrder.getId(), "Order ID must match");
        assertEquals(order1.getDeliveryAddress(), createdOrder.getDeliveryAddress(), "Address must match");

        verify(ordersRepository, times(1)).save(order1);
    }

    /**
     **Test Case:** Retrieve all orders.
     **Expected Result:** Returns a list containing all orders.
     */
    @Test
    void testGetAllOrders_Success() {
        List<Orders> ordersList = Arrays.asList(order1, order2);
        when(ordersRepository.findAll()).thenReturn(ordersList);

        List<Orders> result = ordersService.getAll();

        assertNotNull(result, "Result must not be null");
        assertEquals(2, result.size(), "Must return 2 orders");

        verify(ordersRepository, times(1)).findAll();
    }

    /**
     **Test Case:** Retrieve an order by ID when it exists.
     **Expected Result:** Returns the correct order.
     */
    @Test
    void testGetById_Success() {
        when(ordersRepository.findById(1L)).thenReturn(Optional.of(order1));

        Orders foundOrder = ordersService.getById(1L);

        assertNotNull(foundOrder, "Order must not be null");
        assertEquals(1L, foundOrder.getId(), "Order ID must be 1");
        assertEquals("Main Street 123", foundOrder.getDeliveryAddress(), "Address must be 'Main Street 123'");

        verify(ordersRepository, times(1)).findById(1L);
    }

    /**
     **Test Case:** Attempt to retrieve a non-existing order by ID.
     **Expected Result:** Throws `OrderNotFoundException`.
     */
    @Test
    void testGetById_NotFound() {
        when(ordersRepository.findById(100L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            ordersService.getById(100L);
        });

        assertEquals("Order with id 100 not found", exception.getMessage());

        verify(ordersRepository, times(1)).findById(100L);
    }
}