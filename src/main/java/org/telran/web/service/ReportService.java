package org.telran.web.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public interface ReportService {

    void findTopProductCompletedInTime(LocalDateTime start, LocalDateTime end);

    void findTopProductCanceledInTime(LocalDateTime start, LocalDateTime end);

    Map<String, BigDecimal> profitFromOrders(LocalDateTime startDate, LocalDateTime endDate, String groupBy);
}
