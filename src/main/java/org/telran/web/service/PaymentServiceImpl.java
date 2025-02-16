package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Payment;
import org.telran.web.enums.OrderStatus;
import org.telran.web.exception.PaymentNotFoundException;
import org.telran.web.repository.PaymentJpaRepository;

/**
 * Implementation of PaymentService.
 * Handles business logic for managing payments.
 */
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    /**
     * Retrieves a payment by its ID.
     *
     * @param id ID of the payment.
     * @return The found Payment entity.
     * @throws PaymentNotFoundException if the payment is not found.
     */
    @Override
    public Payment getByIdPayment(Long id) {
        logger.info("Fetching payment with ID: {}", id);
        return paymentJpaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Payment with ID {} not found", id);
                    return new PaymentNotFoundException("Payment by ID " + id + " not found");
                });
    }

    /**
     * Creates a new payment and saves it in the repository.
     *
     * @param payment Payment entity containing payment details.
     * @return The created Payment entity.
     */
    @Override
    public Payment createPayment(Payment payment) {
        logger.info("Creating new payment for user ID: {}", payment.getUser().getId());
        Payment savedPayment = paymentJpaRepository.save(payment);
        logger.info("Payment created successfully with ID: {}", savedPayment.getId());
        return savedPayment;
    }

    /**
     * Deletes a payment by its ID.
     *
     * @param id ID of the payment to delete.
     */
    @Override
    public void deletePayment(Long id) {
        logger.info("Deleting payment with ID: {}", id);
        paymentJpaRepository.deleteById(id);
        logger.info("Payment with ID {} deleted successfully", id);
    }

    /**
     * Updates the payment statuses based on their creation date and current status.
     * <p>
     * This method checks all payments in the repository and updates their statuses
     * according to the following rules:
     * <ul>
     *     <li>If a payment was created more than 2 days ago, its status will be changed to CANCELED.</li>
     *     <li>If the payment status is CREATED, it will be updated to AWAITING_PAYMENT.</li>
     *     <li>If the payment status is AWAITING_PAYMENT, it will be updated to PAID.</li>
     *     <li>If the payment status is PAID, it will be updated to COMPLETED.</li>
     * </ul>
     * </p>
     */
    public void updateStatusPayment() {
        List<Payment> paymentList = paymentJpaRepository.findAll();
        for (Payment payments : paymentList) {
            LocalDateTime dateCreatePayment = payments.getDate();
            LocalDateTime dateNow = LocalDateTime.now();
            Duration duration = Duration.between(dateCreatePayment, dateNow);
            logger.info("Проверка даты создания заказа Payment");
            if (duration.toDays() > 2L) {
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
