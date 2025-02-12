package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telran.web.entity.OrderItems;
import org.telran.web.exception.OrderItemsNotFoundException;
import org.telran.web.repository.OrderItemsJpaRepository;
import java.util.Collection;

/**
 * Implementation of OrderItemsService.
 * Handles business logic for managing order items.
 */
@Service
public class OrderItemsServiceImpl implements OrderItemsService {

    @Autowired
    private OrderItemsJpaRepository orderItemsJpaRepository;

    /**
     * Creates a new order item and saves it in the repository.
     *
     * @param entity OrderItems entity containing order item details.
     * @return The created OrderItems entity.
     */
    @Override
    @Transactional
    public OrderItems createOrderItems(OrderItems entity) {
        return orderItemsJpaRepository.save(entity);
    }

    /**
     * Retrieves all order items from the repository.
     *
     * @return Collection of OrderItems representing all order items.
     */
    @Override
    public Collection<OrderItems> getAllOrderItems() {
        return orderItemsJpaRepository.findAll();
    }

    /**
     * Retrieves an order item by its ID.
     *
     * @param id ID of the order item.
     * @return The found OrderItems entity.
     * @throws OrderItemsNotFoundException if the order item is not found.
     */
    @Override
    public OrderItems getByIdOrderItems(Long id) {
        return orderItemsJpaRepository.findById(id)
                .orElseThrow(() -> new OrderItemsNotFoundException("OrderItem with id " + id + " not found"));
    }

    /**
     * Deletes an order item by its ID.
     *
     * @param id ID of the order item to delete.
     * @throws OrderItemsNotFoundException if the order item does not exist.
     */
    @Override
    @Transactional
    public void deleteOrderItems(Long id) {
        if (!orderItemsJpaRepository.existsById(id)) {
            throw new OrderItemsNotFoundException("OrderItem with id " + id + " not found");
        }
        orderItemsJpaRepository.deleteById(id);
    }
}
