package org.telran.web.service;

import org.telran.web.entity.Payment;

/**
 * Service interface for managing payments.
 * Provides methods for retrieving, creating, and deleting payments.
 */
public interface PaymentService {

    /**
     * Retrieves a payment by its ID.
     *
     * @param id ID of the payment.
     * @return The found Payment entity.
     */
    Payment getByIdPayment(Long id);

    /**
     * Creates a new payment.
     *
     * @param payment Payment entity containing payment details.
     * @return The created Payment entity.
     */
    Payment createPayment(Payment payment);

    /**
     * Deletes a payment by its ID.
     *
     * @param id ID of the payment to delete.
     */
    void deletePayment(Long id);
}