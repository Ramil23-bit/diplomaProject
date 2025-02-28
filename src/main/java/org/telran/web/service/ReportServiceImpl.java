package org.telran.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Report;
import org.telran.web.enums.OrderStatus;
import org.telran.web.repository.ReportJpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportJpaRepository reportJpaRepository;


    @Override
    public void findTopProductCompletedInTime(LocalDateTime start, LocalDateTime end) {
        List<Object[]> listTopProducts = reportJpaRepository.findTopProduct(start, end);

        for (Object[] topProduct : listTopProducts) {
            String nameProduct = nameProduct(topProduct);
            OrderStatus status = statusProduct(topProduct);
            long totalQuantity = totalQuantityProduct(topProduct);

            if (status.equals(OrderStatus.COMPLETED)) {
                createAndSaveProductToReport(nameProduct, status, totalQuantity);
            }
        }
    }

    @Override
    public void findTopProductCanceledInTime(LocalDateTime start, LocalDateTime end) {
        List<Object[]> listTopProducts = reportJpaRepository.findTopProduct(start, end);

        for (Object[] topProduct : listTopProducts) {
            String nameProduct = nameProduct(topProduct);
            OrderStatus status = statusProduct(topProduct);
            long totalQuantity = totalQuantityProduct(topProduct);

            if (status.equals(OrderStatus.CANCELLED)) {
                createAndSaveProductToReport(nameProduct, status, totalQuantity);
            }
        }
    }

    @Override
    public Map<String, BigDecimal> profitFromOrders(LocalDateTime startDate, LocalDateTime endDate, String groupBy) {
        List<Object[]> results = reportJpaRepository.findProfitByPeriod(startDate, endDate, "month");
        Map<String, BigDecimal> profitMap = new HashMap<>();

        for (Object[] result : results) {
            BigDecimal totalAmount = (BigDecimal) result[0];
            String period = result[1].toString();
            profitMap.put(period, totalAmount);
        }
        return profitMap;
    }

    private String nameProduct(Object[] topProduct) {
        return (String) topProduct[0];
    }

    private OrderStatus statusProduct(Object[] topProduct) {
        return (OrderStatus) topProduct[1];
    }

    private long totalQuantityProduct(Object[] topProduct) {
        return ((Number) topProduct[2]).intValue();
    }

    public void createAndSaveProductToReport(String name, OrderStatus status, long quantity) {
        Report newReport = new Report();
        newReport.setNameProduct(name);
        newReport.setAmount(quantity);
        newReport.setCreateDate(LocalDateTime.now());
        newReport.setStatus(status);
        reportJpaRepository.save(newReport);
    }
}