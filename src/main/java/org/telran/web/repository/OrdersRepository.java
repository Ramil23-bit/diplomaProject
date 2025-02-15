package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Orders;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.user.id = :currentUserId AND o.status = org.telran.web.enums.OrderStatus.COMPLETED")
    List<Orders> findAllByUserIdHistory(Long currentUserId);

    @Query("SELECT " +
            "CASE " +
            "WHEN :groupBy = 'HOUR' THEN HOUR(o.updatedAt) " +
            "WHEN :groupBy = 'DAY' THEN DAY(o.updatedAt) " +
            "WHEN :groupBy = 'WEEK' THEN WEEK(o.updatedAt) " +
            "WHEN :groupBy = 'MONTH' THEN MONTH(o.updatedAt) " +
            "END AS period, " +
            "SUM(oi.priceAtPurchase * oi.quantity) " + // 🔹 Используем `oi.priceAtPurchase`
            "FROM Orders o " +
            "JOIN o.orderItems oi " +
            "WHERE o.status = 'COMPLETED' " +
            "AND o.updatedAt BETWEEN :startDate AND :endDate " +
            "GROUP BY period " +
            "ORDER BY 1")
    List<Object[]> findRevenueBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("groupBy") String groupBy);
}