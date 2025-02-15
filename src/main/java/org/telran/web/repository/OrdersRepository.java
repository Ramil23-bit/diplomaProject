package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Orders;
import org.telran.web.entity.User;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.user.id = :currentUserId AND o.status = org.telran.web.enums.OrderStatus.COMPLETED")
    List<Orders> findAllByUserIdHistory(Long currentUserId);
}
