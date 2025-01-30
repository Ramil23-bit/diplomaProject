package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.OrderItems;

import java.util.List;

@Repository
public interface OrderItemsJpaRepository extends JpaRepository<OrderItems, Long> {

    List<OrderItems> findAll();

    OrderItems save(OrderItems orderItems);
}
