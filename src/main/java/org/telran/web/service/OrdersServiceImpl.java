package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Orders;
import org.telran.web.exception.OrderNotFoundException;
import org.telran.web.repository.OrdersRepository;

import java.util.List;

/**
 * Implementation of OrdersService.
 * Handles business logic for managing orders.
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    private static final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);

    @Autowired
    private OrdersRepository repository;

    /**
     * Creates a new order and saves it in the repository.
     *
     * @param orders Orders entity containing order details.
     * @return The created Orders entity.
     */
    @Override
    public Orders create(Orders orders) {
        logger.info("Creating new order for user ID: {}", orders.getUser().getId());
        Orders savedOrder = repository.save(orders);
        logger.info("Order created successfully with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    /**
     * Retrieves all orders from the repository.
     *
     * @return List of all Orders entities.
     */
    @Override
    public List<Orders> getAll() {
        logger.info("Fetching all orders");
        List<Orders> ordersList = repository.findAll();
        logger.info("Total orders retrieved: {}", ordersList.size());
        return ordersList;
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id ID of the order.
     * @return The found Orders entity.
     * @throws OrderNotFoundException if the order is not found.
     */
    @Override
    public Orders getById(Long id) {
        logger.info("Fetching order with ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order with ID {} not found", id);
                    return new OrderNotFoundException("Order with id " + id + " not found");
                });
    }
}
