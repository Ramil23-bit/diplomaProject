package org.telran.web.service;

import org.telran.web.entity.OrderItems;

import java.util.Collection;
import java.util.List;

/**
 * Service interface for managing order items.
 * Provides methods for creating, retrieving, and deleting order items.
 */
public interface OrderItemsService {

    /**
     * Creates a new order item.
     *
     * @param entity OrderItems entity containing order item details.
     * @return The created OrderItems entity.
     */
    OrderItems createOrderItems(OrderItems entity);

    /**
     * Retrieves all order items.
     *
     * @return Collection of OrderItems representing all order items.
     */
    Collection<OrderItems> getAllOrderItems();

    /**
     * Retrieves an order item by its ID.
     *
     * @param id ID of the order item.
     * @return The found OrderItems entity.
     */
    OrderItems getByIdOrderItems(Long id);

    /**
     * Deletes an order item by its ID.
     *
     * @param id ID of the order item to delete.
     */
    void deleteOrderItems(Long id);
}