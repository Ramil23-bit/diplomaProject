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
    /**
     * Retrieves a list of orders that are currently awaiting payment.
     *
     * <p>
     * This method iterates through all available orders and filters them based on their
     * status. Only orders with the status {AWAITING_PAYMENT} will be included
     * in the returned list.
     * </p>
     *
     * @return a list of {@link Orders} that have the status {AWAITING_PAYMENT}.
     */
    String getOrderStatusById(Long orderId);

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

    List<Orders> getAllByUserIdHistory();

    List<Orders> getAllByCurrentUser();

    void delete(Long id);
}