package org.telran.web.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Report;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportJpaRepository extends JpaRepository<Report, Long> {

    @Query("SELECT oi.product.productTitle, oi.orders.status, SUM(oi.quantity) FROM OrderItems oi " +
            "JOIN oi.orders o " +
            "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate " +
            "GROUP BY oi.product.productTitle, oi.orders.status ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopProduct(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);
    @Query(value = "SELECT SUM(oi.price_at_purchase), DATE_TRUNC(:groupBy, o.created_at) " +
            "FROM order_items oi JOIN orders o ON oi.order_id = o.id " +
            "WHERE o.created_at >= :startDate AND o.created_at <= :endDate " +
            "GROUP BY o.created_at " +
            "ORDER BY DATE_TRUNC(:groupBy, o.created_at)", nativeQuery = true)
    List<Object[]> findProfitByPeriod(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate,
                                      @Param("groupBy") String groupBy);
}
