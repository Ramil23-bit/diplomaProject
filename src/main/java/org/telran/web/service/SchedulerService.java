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
            if (duration.toMinutes() > 60L) {
                orders.setStatus(OrderStatus.CANCELLED);
                ordersRepository.save(orders);
            }
            switch (orders.getStatus()) {
                case CREATED -> {
                    orders.setStatus(OrderStatus.PAID);
                    ordersRepository.save(orders);
                }
                case PAID -> {
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
            if(duration.toMinutes() > 10L){
                payments.setOrderStatus(OrderStatus.CANCELLED);
                paymentJpaRepository.save(payments);
            }
            switch (payments.getOrderStatus()) {
                case CREATED -> {
                    payments.setOrderStatus(OrderStatus.PAID);
                    paymentJpaRepository.save(payments);
                }
                case PAID -> {
                    payments.setOrderStatus(OrderStatus.COMPLETED);
                    paymentJpaRepository.save(payments);
                }
            }
        }
    }

}
