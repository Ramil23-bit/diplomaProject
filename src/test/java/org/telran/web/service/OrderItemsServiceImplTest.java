package org.telran.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Orders;
import org.telran.web.entity.Product;
import org.telran.web.exception.OrderItemsNotFoundException;
import org.telran.web.repository.OrderItemsJpaRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderItemsServiceImplTest {

    @Mock
    private OrderItemsJpaRepository orderItemsJpaRepository;

    @InjectMocks
    private OrderItemsServiceImpl orderItemsService;

    private OrderItems orderItemsExpected;

    private OrderItems orderItemsExpectedTwo;
    private Product product;
    private Orders orders;

    @BeforeEach
    void setUp(){
        BigDecimal price = new BigDecimal(10);
        BigDecimal priceTwo = new BigDecimal(20);
        product = new Product();
        orders = new Orders();
        orderItemsExpected = new OrderItems(1L, 10L, price, product, orders);
        orderItemsExpectedTwo = new OrderItems(2L, 5L, priceTwo, product, orders);
    }

    @Test
    void testCreateOrderItems(){
        when(orderItemsJpaRepository.save(any(OrderItems.class)))
                .thenReturn(orderItemsExpected);

        OrderItems resultOrderItems = orderItemsService.createOrderItems(orderItemsExpected);

        assertNotNull(resultOrderItems);
        assertEquals(10L, resultOrderItems.getQuantity());
        Mockito.verify(orderItemsJpaRepository, times(1)).save(orderItemsExpected);
    }

    @Test
    void testGetAllOrderItems(){
        List<OrderItems> orderItemsList = Arrays.asList(orderItemsExpected, orderItemsExpectedTwo);
        when(orderItemsJpaRepository.findAll())
                .thenReturn(orderItemsList);

        Collection<OrderItems> actualListOrderItems = orderItemsService.getAllOrderItems();

        assertNotNull(actualListOrderItems);
        assertEquals(2, actualListOrderItems.size());
        Mockito.verify(orderItemsJpaRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdOrderItemsWhenNotFound(){
        Long orderItemsId = 3L;

        when(orderItemsJpaRepository.findById(orderItemsId))
                .thenThrow(new OrderItemsNotFoundException("Order items not Found"));

        assertThrows(OrderItemsNotFoundException.class,()-> orderItemsService.getByIdOrderItems(orderItemsId));
    }

    @Test
    void testGetByIdOrderItemsWhenOrderItemsExist(){
        Long orderItemsId = 1L;

        when(orderItemsJpaRepository.findById(orderItemsId))
                .thenReturn(Optional.of(orderItemsExpected));

        OrderItems actualOrderItems = orderItemsService.getByIdOrderItems(orderItemsId);

        assertNotNull(actualOrderItems);
        assertEquals(1L, actualOrderItems.getId());
        assertEquals(10L, actualOrderItems.getQuantity());
        Mockito.verify(orderItemsJpaRepository, times(1)).findById(orderItemsId);
    }

    @Test
    void testDeleteByIdOrderItemsWhenOrderItemsExist(){
        Optional<OrderItems> foundOrderItems = orderItemsJpaRepository.findById(1L);
        when(orderItemsJpaRepository.existsById(1L))
                .thenReturn(true);

        orderItemsService.deleteOrderItems(1L);

        Mockito.verify(orderItemsJpaRepository, times(1)).deleteById(1L);
    }
}
