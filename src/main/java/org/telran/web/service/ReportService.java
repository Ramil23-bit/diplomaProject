package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.dto.*;
import org.telran.web.entity.Product;
import org.telran.web.repository.OrderItemsJpaRepository;
import org.telran.web.repository.OrdersRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    OrderItemsJpaRepository orderItemsJpaRepository;

    @Autowired
    OrdersRepository ordersRepository;

    /**
     * Топ-10 купленных товаров
     */
    public List<ProductReportDto> getTop10PurchasedProducts() {
        return orderItemsJpaRepository.findTop10PurchasedProducts()
                .stream()
                .map(result -> new ProductReportDto((Product) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    /**
     * Топ-10 отменённых товаров
     */
    public List<ProductReportDto> getTop10CancelledProducts() {
        return orderItemsJpaRepository.findTop10CancelledProducts()
                .stream()
                .map(result -> new ProductReportDto((Product) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    /**
     * Список товаров, ожидающих оплаты более N дней
     */
    public List<Product> getUnpaidProductsOlderThan(int days) {
        LocalDateTime dateThreshold = LocalDateTime.now().minusDays(days);
        return orderItemsJpaRepository.findUnpaidOrdersOlderThan(dateThreshold);
    }

    /**
     * Прибыль за N дней, месяцев, лет с группировкой
     */
    public List<RevenueReportDto> getRevenueReport(int n, ChronoUnit unit, String groupBy) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(n, unit);

        return ordersRepository.findRevenueBetween(startDate, endDate, groupBy)
                .stream()
                .map(result -> new RevenueReportDto((Integer) result[0], (BigDecimal) result[1]))
                .collect(Collectors.toList());
    }
}
