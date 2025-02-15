package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.CartItems;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Orders;
import org.telran.web.entity.Product;
import org.telran.web.enums.OrderStatus;
import org.telran.web.exception.OrderNotFoundException;
import org.telran.web.repository.OrdersRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of OrdersService.
 * Handles business logic for managing orders.
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    private static final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);

    @Autowired
    private OrdersRepository repository;

    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemsService cartItemsService;

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
        List<OrderItems> orderItems = savedOrder.getOrderItems();
        if (orderItems == null || orderItems.isEmpty()) {
            logger.warn("Order has no items!");
            return savedOrder;
        }
        List<Product> products = orderItems.stream().map(OrderItems::getProduct).collect(Collectors.toList());
        List<CartItems> currentCartItems = cartItemsService.getAllCartItems().stream().filter(cartItems -> products.contains(cartItems.getProduct())).collect(Collectors.toList());
        currentCartItems.forEach(cartItemsService::removeCartItem);
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

    @Override
    public List<Orders> getAllByUserIdHistory() {
        return repository.findAllByUserIdHistory(userService.getCurrentUserId());
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

    /**
     * Checks the status of all orders and returns a list of orders that are awaiting payment.
     *
     * @return a list of orders with the status AWAITING_PAYMENT.
     */
    @Override
    public List<Orders> checkOrderStatus() {
        List<Orders> ordersAwaitingStatus = new ArrayList<>();
        for (Orders orders : getAll()) {
            if (orders.getStatus().equals(OrderStatus.AWAITING_PAYMENT)) {
                ordersAwaitingStatus.add(orders);
            }
        }
        return ordersAwaitingStatus;
    }
    /**
     * Updates the statuses of orders based on their creation time and current status.
     * <p>
     * If an order was created more than 60 minutes ago and is in the AWAITING_PAYMENT status,
     * it will be cancelled. Orders with other statuses will be updated according to their
     * current status:
     * <ul>
     *     <li>CREATED -> AWAITING_PAYMENT</li>
     *     <li>AWAITING_PAYMENT -> PAID</li>
     *     <li>PAID -> COMPLETED</li>
     * </ul>
     * </p>
     */

    public void updateStatusOrders() {
        List<Orders> ordersServiceList = repository.findAll();
        for (Orders orders : ordersServiceList) {
            LocalDateTime dateCreateOrders = orders.getCreatedAt();
            LocalDateTime dateNow = LocalDateTime.now();
            Duration duration = Duration.between(dateCreateOrders, dateNow);
            logger.info("Проверка даты создания заказа ORDERS");
            if (duration.toMinutes() > 60L) {
                logger.info("Смена статуса заказа на CANCELED ORDERS");
                orders.setStatus(OrderStatus.CANCELLED);
                repository.save(orders);
            }
            switch (orders.getStatus()) {
                case CREATED -> {
                    logger.info("Смена статуса заказа на AWAITING PAYMENT ORDERS");
                    orders.setStatus(OrderStatus.AWAITING_PAYMENT);
                    repository.save(orders);
                }
                case AWAITING_PAYMENT -> {
                    logger.info("Смена статуса заказа на PAID ORDERS");
                    orders.setStatus(OrderStatus.PAID);
                    repository.save(orders);
                }
                case PAID -> {
                    logger.info("Смена статуса заказа на COMPLETED ORDERS");
                    orders.setStatus(OrderStatus.COMPLETED);
                    repository.save(orders);
                }
            }
        }
    }
}
