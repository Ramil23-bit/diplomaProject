package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.dto.*;
import org.telran.web.entity.Product;
import org.telran.web.enums.GroupBy;
import org.telran.web.repository.OrderItemsJpaRepository;
import org.telran.web.repository.OrdersRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    OrderItemsJpaRepository orderItemsJpaRepository;

    @Autowired
    OrdersRepository ordersRepository;

    /**
     * Retrieves the top 10 most purchased products.
     *
     * @return A list of the top 10 purchased products with their purchase counts.
     */
    public List<ProductReportDto> getTop10PurchasedProducts() {
        return orderItemsJpaRepository.findTop10PurchasedProducts()
                .stream()
                .map(result -> new ProductReportDto((Product) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the top 10 most canceled products.
     *
     * @return A list of the top 10 canceled products with their cancellation counts.
     */
    public List<ProductReportDto> getTop10CancelledProducts() {
        return orderItemsJpaRepository.findTop10CancelledProducts()
                .stream()
                .map(result -> new ProductReportDto((Product) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of unpaid products older than the specified number of days.
     *
     * @param days The number of days after which unpaid products should be retrieved.
     * @return A list of unpaid products older than the specified number of days.
     */
    public List<Product> getUnpaidProductsOlderThan(int days) {
        LocalDateTime dateThreshold = LocalDateTime.now().minusDays(days);
        return orderItemsJpaRepository.findUnpaidOrdersOlderThan(dateThreshold);
    }


    public List<RevenueReportDto> getRevenueReport(int n, ChronoUnit unit, GroupBy groupBy) {
        // Validate time unit
        if (!List.of(ChronoUnit.HOURS, ChronoUnit.DAYS, ChronoUnit.WEEKS, ChronoUnit.MONTHS, ChronoUnit.YEARS).contains(unit)) {
            throw new IllegalArgumentException("Unsupported time unit: " + unit);
        }

        // Calculate the date range for the report
        LocalDateTime endDate = LocalDateTime.now(); // current time
        LocalDateTime startDate = endDate.minus(n, unit); // current time - n

        // Log the request details
        logger.info("Fetching revenue report: n={}, unit={}, groupBy={}", n, unit, groupBy.getValue());
        logger.info("Date range: {} - {}", startDate, endDate);

        // Execute database query
        List<Object[]> results = ordersRepository.findRevenueBetween(startDate, endDate, groupBy.getValue());

        // Log the number of results found
        logger.info("Query returned {} results", results.size());

        if (results.isEmpty()) {
            logger.warn("No revenue data found for the given parameters.");
        } else {
            // Log sample results for verification
            results.stream()
                    .limit(5)
                    .forEach(result -> logger.info("Sample result: id={}, revenue={}", result[0], result[1]));
        }

        // Convert the query results into DTO objects
        return results.stream()
                .map(result -> {
                    try {
                        Integer id = (Integer) result[0];
                        BigDecimal revenue = (BigDecimal) result[1];
                        return new RevenueReportDto(id, revenue);
                    } catch (Exception e) {
                        logger.error("Error parsing result: {}", result, e);
                        throw new IllegalStateException("Unexpected data format from repository", e);
                    }
                })
                .collect(Collectors.toList());
    }
}