package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Payment;
import org.telran.web.exception.PaymentNotFoundException;
import org.telran.web.repository.PaymentJpaRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

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
}
