package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.CartItems;

import java.util.List;

@Repository
public interface CartItemsJpaRepository extends JpaRepository<CartItems, Long> {
    List<CartItems> findAllByCartId(Long currentUserCartId);
}
