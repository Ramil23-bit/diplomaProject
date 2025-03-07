package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Payment;

@Repository
public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
