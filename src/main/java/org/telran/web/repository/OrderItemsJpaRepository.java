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

    /**
     * Retrieves the top 10 most purchased products.
     * This method counts the occurrences of each product in completed orders and returns the most frequently purchased ones.
     *
     * @return A list of objects where each entry contains a {@link Product} and its purchase count.
     */
    @Query("SELECT oi.product, COUNT(oi.product) AS purchaseCount " + // SQL-request: 'oi.product' from OrderItems
            "FROM OrderItems oi " +
            "JOIN oi.orders o " +
            "WHERE o.status = 'COMPLETED' " + // JOIN OrdetItems(oi) + Orders(o)-COMPLETED
            "GROUP BY oi.product " + // group by product
            "ORDER BY purchaseCount DESC LIMIT 10") //sort by purchaseCount DESC(decrease)
    List<Object[]> findTop10PurchasedProducts();

    /**
     * Retrieves the top 10 most canceled products.
     * This method counts the occurrences of each product in canceled orders and returns the most frequently canceled ones.
     *
     * @return A list of objects where each entry contains a {@link Product} and its cancellation count.
     */
    @Query("SELECT oi.product, COUNT(oi.product) AS cancelCount " + // SQL-request: 'oi.product' from OrderItems
            "FROM OrderItems oi " +
            "JOIN oi.orders o " +
            "WHERE o.status = 'CANCELLED' " + // JOIN OrdetItems(oi) + Orders(o)-CANCELLED
            "GROUP BY oi.product " + // group by product
            "ORDER BY cancelCount DESC LIMIT 10") //sort by cancelCount DESC(decrease)
    List<Object[]> findTop10CancelledProducts();

    /**
     * Retrieves a list of products that are part of unpaid orders older than the specified date.
     * This method finds products from orders that are still in the "AWAITING_PAYMENT" status and were created before the given date.
     *
     * @param date The cutoff date; orders created before this date will be considered unpaid.
     * @return A list of unpaid {@link Product} instances.
     */
    @Query("SELECT oi.product " + // SQL-request: 'oi.product' from OrderItems
            "FROM OrderItems oi " +
            "JOIN oi.orders o " +
            "WHERE o.status = 'AWAITING_PAYMENT' AND o.createdAt < :date") // JOIN OrdetItems(oi) + Orders(o)-AWAITING_PAYMENT + 'createdAt' from Orders
    List<Product> findUnpaidOrdersOlderThan(@Param("date") LocalDateTime date);




}