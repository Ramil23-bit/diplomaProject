package org.telran.web.service;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Orders;
import org.telran.web.enums.OrderStatus;
import org.telran.web.exception.OrderNotFoundException;
import org.telran.web.exception.UserNotFoundException;
import org.telran.web.repository.OrdersRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository repository;

    @Autowired
    private PaymentServiceImpl paymentService;

    private static final Logger logger = Logger.getLogger(OrdersSchedulerService.class.getName());

    @Override
    public Orders create(Orders orders) {
        return repository.save(orders);
    }

    @Override
    public List<Orders> getAll() {
        return repository.findAll();
    }

    @Override
    public Orders getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
    }
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
