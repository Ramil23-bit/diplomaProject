package org.telran.web.service;

import org.telran.web.entity.Payment;

public interface PaymentService {

    Payment getByIdPayment(Long id);
    Payment createPayment(Payment payment);
    void deletePayment(Long id);
}
