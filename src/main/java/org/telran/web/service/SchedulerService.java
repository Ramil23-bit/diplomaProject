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

@Component
public class SchedulerService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    private static final Logger logger = Logger.getLogger(SchedulerService.class.getName());

    @Scheduled(fixedRate = 10000L)
    public void updateOrderStatusInOrders() {
        List<Orders> ordersServiceList = ordersRepository.findAll();
        for (Orders orders : ordersServiceList) {
            LocalDateTime dateCreateOrders = orders.getCreatedAt();
            LocalDateTime dateNow = LocalDateTime.now();
            Duration duration = Duration.between(dateCreateOrders, dateNow);
            logger.info("Проверка даты создания заказа");
            if (duration.toMinutes() > 60L) {
                logger.info("Смена статуса заказа на CANCELED");
                orders.setStatus(OrderStatus.CANCELLED);
                ordersRepository.save(orders);
            }
            switch (orders.getStatus()) {
                case CREATED -> {
                    logger.info("Смена статуса заказа на PAID");
                    orders.setStatus(OrderStatus.PAID);
                    ordersRepository.save(orders);
                }
                case PAID -> {
                    logger.info("Смена статуса заказа на COMPLETED");
                    orders.setStatus(OrderStatus.COMPLETED);
                    ordersRepository.save(orders);
                }
            }
        }
    }

    @Scheduled(fixedRate = 20000L)
    public void updateOrderStatusInPayment(){
        List<Payment> paymentList = paymentJpaRepository.findAll();
        for(Payment payments : paymentList){
            LocalDateTime dateCreatePayment = payments.getDate();
            LocalDateTime dateNow = LocalDateTime.now();
            Duration duration = Duration.between(dateCreatePayment, dateNow);
            logger.info("Проверка даты создания заказа");
            if(duration.toMinutes() > 10L){
                logger.info("Смена статуса заказа на CANCELED");
                payments.setOrderStatus(OrderStatus.CANCELLED);
                paymentJpaRepository.save(payments);
            }
            switch (payments.getOrderStatus()) {
                case CREATED -> {
                    logger.info("Смена статуса заказа на PAID");
                    payments.setOrderStatus(OrderStatus.PAID);
                    paymentJpaRepository.save(payments);
                }
                case PAID -> {
                    logger.info("Смена статуса заказа на COMPLETED");
                    payments.setOrderStatus(OrderStatus.COMPLETED);
                    paymentJpaRepository.save(payments);
                }
            }
        }
    }

}
