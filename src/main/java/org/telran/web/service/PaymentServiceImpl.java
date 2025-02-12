package org.telran.web.service;

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
        return paymentJpaRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment by ID " + id + " not found"));
    }

    /**
     * Creates a new payment and saves it in the repository.
     *
     * @param payment Payment entity containing payment details.
     * @return The created Payment entity.
     */
    @Override
    public Payment createPayment(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    /**
     * Deletes a payment by its ID.
     *
     * @param id ID of the payment to delete.
     */
    @Override
    public void deletePayment(Long id) {
        paymentJpaRepository.deleteById(id);
    }
}
