package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.OrderItems;
import org.telran.web.entity.Product;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemsJpaRepository extends JpaRepository<OrderItems, Long> {

    @Query("SELECT oi.product, COUNT(oi.product) AS purchaseCount " +
            "FROM OrderItems oi " +
            "JOIN oi.orders o " +
            "WHERE o.status = 'COMPLETED' " +
            "GROUP BY oi.product " +
            "ORDER BY purchaseCount DESC LIMIT 10")
    List<Object[]> findTop10PurchasedProducts();

    @Query("SELECT oi.product, COUNT(oi.product) AS cancelCount " +
            "FROM OrderItems oi " +
            "JOIN oi.orders o " +
            "WHERE o.status = 'CANCELLED' " +
            "GROUP BY oi.product " +
            "ORDER BY cancelCount DESC LIMIT 10")
    List<Object[]> findTop10CancelledProducts();

    @Query("SELECT oi.product " +
            "FROM OrderItems oi " +
            "JOIN oi.orders o " +
            "WHERE o.status = 'CREATED' AND o.createdAt < :date")
    List<Product> findUnpaidOrdersOlderThan(@Param("date") LocalDateTime date);
}