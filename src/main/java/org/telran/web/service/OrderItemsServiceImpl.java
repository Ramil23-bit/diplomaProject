package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(OrderItemsServiceImpl.class);

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
        logger.info("Creating new order item");
        OrderItems savedOrderItem = orderItemsJpaRepository.save(entity);
        logger.info("Order item created successfully with ID: {}", savedOrderItem.getId());
        return savedOrderItem;
    }

    /**
     * Retrieves all order items from the repository.
     *
     * @return Collection of OrderItems representing all order items.
     */
    @Override
    public Collection<OrderItems> getAllOrderItems() {
        logger.info("Fetching all order items");
        Collection<OrderItems> orderItems = orderItemsJpaRepository.findAll();
        logger.info("Total order items retrieved: {}", orderItems.size());
        return orderItems;
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
        logger.info("Fetching order item with ID: {}", id);
        return orderItemsJpaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order item with ID {} not found", id);
                    return new OrderItemsNotFoundException("OrderItem with id " + id + " not found");
                });
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
        logger.info("Attempting to delete order item with ID: {}", id);
        if (!orderItemsJpaRepository.existsById(id)) {
            logger.error("Order item with ID {} not found", id);
            throw new OrderItemsNotFoundException("OrderItem with id " + id + " not found");
        }
        orderItemsJpaRepository.deleteById(id);
        logger.info("Order item with ID {} deleted successfully", id);
    }
}
