package org.telran.web.service;

import org.telran.web.entity.Orders;

import java.util.List;

/**
 * Service interface for managing orders.
 * Provides methods for creating, retrieving, and listing orders.
 */
public interface OrdersService {

    /**
     * Creates a new order.
     *
     * @param orders Orders entity containing order details.
     * @return The created Orders entity.
     */
    Orders create(Orders orders);
    List<Orders> checkOrderStatus();

    /**
     * Retrieves all orders.
     *
     * @return List of all Orders entities.
     */
    List<Orders> getAll();

    /**
     * Retrieves an order by its ID.
     *
     * @param id ID of the order.
     * @return The found Orders entity.
     */
    Orders getById(Long id);
}
