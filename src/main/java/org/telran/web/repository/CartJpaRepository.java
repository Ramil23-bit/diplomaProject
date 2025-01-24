package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Cart;

@Repository
public interface CartJpaRepository extends JpaRepository<Cart, Long> {
}
