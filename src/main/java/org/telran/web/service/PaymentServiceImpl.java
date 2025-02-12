package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Payment;
import org.telran.web.exception.PaymentNotFoundException;
import org.telran.web.repository.PaymentJpaRepository;

/**
 * Implementation of PaymentService.
 * Handles business logic for managing payments.
 */
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
}
