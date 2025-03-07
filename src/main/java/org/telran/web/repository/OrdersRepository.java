package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Orders;
import org.telran.web.entity.User;
import org.telran.web.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE o.user.id = :currentUserId AND o.status IN (org.telran.web.enums.OrderStatus.COMPLETED, org.telran.web.enums.OrderStatus.CANCELLED)")
    List<Orders> findAllByUserIdHistory(Long currentUserId);
    @Query("SELECT o FROM Orders o WHERE o.user.id = :currentUserId AND o.status NOT IN (org.telran.web.enums.OrderStatus.COMPLETED, org.telran.web.enums.OrderStatus.CANCELLED)")
    List<Orders> findAllByUserId(Long currentUserId);
    @Query("SELECT o.status FROM Orders o WHERE o.id = :orderId")
    OrderStatus findStatusByOrderId(@Param("orderId") Long orderId);

    /**
     * Retrieves revenue data grouped by a specified time period.
     * This method calculates the total revenue within a given date range, grouped by hours, days, weeks, or months.
     *
     * @param startDate The start date of the revenue report period.
     * @param endDate   The end date of the revenue report period.
     * @param groupBy   The grouping criteria for revenue aggregation. Allowed values: "HOUR", "DAY", "WEEK", "MONTH".
     * @return A list of objects where each entry contains a time period and the corresponding revenue sum.
     */
    @Query("SELECT " +
            "CASE " +
            "WHEN :groupBy = 'HOUR' THEN HOUR(o.updatedAt) " +
            "WHEN :groupBy = 'DAY' THEN DAY(o.updatedAt) " +
            "WHEN :groupBy = 'WEEK' THEN WEEK(o.updatedAt) " +
            "WHEN :groupBy = 'MONTH' THEN MONTH(o.updatedAt) " +
            "END AS period, " +
            "SUM(oi.priceAtPurchase * oi.quantity) " +
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