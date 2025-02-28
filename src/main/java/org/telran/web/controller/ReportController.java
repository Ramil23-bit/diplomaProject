package org.telran.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.service.ReportService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @GetMapping("/completed")
    public void topProductCompleted(@RequestParam String start, @RequestParam String end){
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        reportService.findTopProductCompletedInTime(startDate, endDate);
    }

    @GetMapping("/canceled")
    public void topProductCanceled(@RequestParam String start, @RequestParam String end){
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        reportService.findTopProductCanceledInTime(startDate, endDate);
    }

    @GetMapping("/profit")
    public Map<String, BigDecimal> totalProfit(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("groupBy") String groupBy) {
        return reportService.profitFromOrders(startDate, endDate, groupBy);
    }
}