package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Orders;
import org.telran.web.entity.Payment;
import org.telran.web.enums.OrderStatus;
import org.telran.web.exception.PaymentNotFoundException;
import org.telran.web.repository.PaymentJpaRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;
    private static final Logger logger = Logger.getLogger(OrdersSchedulerService.class.getName());

    @Override
    public Payment getByIdPayment(Long id) {
        return paymentJpaRepository.findById(id)
                .orElseThrow(()-> new PaymentNotFoundException("Payment by ID " + id + " not found"));
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public void deletePayment(Long id) {
        paymentJpaRepository.deleteById(id);
    }

    public void updateStatusPayment(){
        List<Payment> paymentList = paymentJpaRepository.findAll();
        for(Payment payments : paymentList){
            LocalDateTime dateCreatePayment = payments.getDate();
            LocalDateTime dateNow = LocalDateTime.now();
            Duration duration = Duration.between(dateCreatePayment, dateNow);
            logger.info("Проверка даты создания заказа Payment");
            if(duration.toDays() > 2L){
                logger.info("Смена статуса заказа на CANCELED Payment");
                payments.setOrderStatus(OrderStatus.CANCELLED);
                paymentJpaRepository.save(payments);
            }
            switch (payments.getOrderStatus()) {
                case CREATED -> {
                    logger.info("Смена статуса заказа на AWAITING_PAYMENT Payment");
                    payments.setOrderStatus(OrderStatus.AWAITING_PAYMENT);
                    paymentJpaRepository.save(payments);
                }
                case AWAITING_PAYMENT -> {
                    logger.info("Смена статуса заказа на PAID Payment");
                    payments.setOrderStatus(OrderStatus.PAID);
                    paymentJpaRepository.save(payments);
                }
                case PAID -> {
                    logger.info("Смена статуса заказа на COMPLETED Payment");
                    payments.setOrderStatus(OrderStatus.COMPLETED);
                    paymentJpaRepository.save(payments);
                }
            }
        }
    }
}
