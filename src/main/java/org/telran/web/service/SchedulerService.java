package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telran.web.entity.Orders;
import org.telran.web.entity.Payment;
import org.telran.web.enums.OrderStatus;
import org.telran.web.repository.OrdersRepository;
import org.telran.web.repository.PaymentJpaRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for scheduled order and payment status updates.
 * Executes scheduled tasks to update order and payment statuses based on time elapsed.
 */
@Component
public class SchedulerService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    private static final Logger logger = Logger.getLogger(SchedulerService.class.getName());

    /**
     * Scheduled task to update order statuses every 10 seconds.
     * Changes the order status based on time elapsed since creation.
     */
    @Scheduled(fixedRate = 10000L)
    public void updateOrderStatusInOrders() {
        List<Orders> ordersServiceList = ordersRepository.findAll();
        for (Orders orders : ordersServiceList) {
            LocalDateTime dateCreateOrders = orders.getCreatedAt();
            LocalDateTime dateNow = LocalDateTime.now();
            Duration duration = Duration.between(dateCreateOrders, dateNow);
            logger.info("Checking order creation date");
            if (duration.toMinutes() > 60L) {
                logger.info("Changing order status to CANCELED");
                orders.setStatus(OrderStatus.CANCELLED);
                ordersRepository.save(orders);
            }
            switch (orders.getStatus()) {
                case CREATED -> {
                    logger.info("Changing order status to PAID");
                    orders.setStatus(OrderStatus.PAID);
                    ordersRepository.save(orders);
                }
                case PAID -> {
                    logger.info("Changing order status to COMPLETED");
                    orders.setStatus(OrderStatus.COMPLETED);
                    ordersRepository.save(orders);
                }
            }
        }
    }

    /**
     * Scheduled task to update payment statuses every 20 seconds.
     * Updates payment statuses based on time elapsed since creation.
     */
    @Scheduled(fixedRate = 20000L)
    public void updateOrderStatusInPayment() {
        List<Payment> paymentList = paymentJpaRepository.findAll();
        for (Payment payments : paymentList) {
            LocalDateTime dateCreatePayment = payments.getDate();
            LocalDateTime dateNow = LocalDateTime.now();
            Duration duration = Duration.between(dateCreatePayment, dateNow);
            logger.info("Checking payment creation date");
            if (duration.toMinutes() > 10L) {
                logger.info("Changing payment status to CANCELED");
                payments.setOrderStatus(OrderStatus.CANCELLED);
                paymentJpaRepository.save(payments);
            }
            switch (payments.getOrderStatus()) {
                case CREATED -> {
                    logger.info("Changing payment status to PAID");
                    payments.setOrderStatus(OrderStatus.PAID);
                    paymentJpaRepository.save(payments);
                }
                case PAID -> {
                    logger.info("Changing payment status to COMPLETED");
                    payments.setOrderStatus(OrderStatus.COMPLETED);
                    paymentJpaRepository.save(payments);
                }
            }
        }
    }
}
