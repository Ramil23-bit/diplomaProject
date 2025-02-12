package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Orders;
import org.telran.web.exception.OrderNotFoundException;
import org.telran.web.exception.UserNotFoundException;
import org.telran.web.repository.OrdersRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of OrdersService.
 * Handles business logic for managing orders.
 */
@Service
public class OrdersServiceImpl implements OrdersService {

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
        return repository.save(orders);
    }

    /**
     * Retrieves all orders from the repository.
     *
     * @return List of all Orders entities.
     */
    @Override
    public List<Orders> getAll() {
        return repository.findAll();
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
        return repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
    }
}