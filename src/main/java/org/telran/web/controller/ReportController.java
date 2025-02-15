package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.dto.*;
import org.telran.web.entity.Product;
import org.telran.web.service.ReportService;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @GetMapping("/top-purchased")
    public List<ProductReportDto> getTopPurchasedProducts() {
        return reportService.getTop10PurchasedProducts();
    }

    @GetMapping("/top-cancelled")
    public List<ProductReportDto> getTopCancelledProducts() {
        return reportService.getTop10CancelledProducts();
    }

    @GetMapping("/unpaid-products")
    public List<Product> getUnpaidProducts(@RequestParam int days) {
        return reportService.getUnpaidProductsOlderThan(days);
    }

    @GetMapping("/revenue")
    public List<RevenueReportDto> getRevenue(
            @RequestParam int n,
            @RequestParam String unit,
            @RequestParam String groupBy) {
        return reportService.getRevenueReport(n, ChronoUnit.valueOf(unit.toUpperCase()), groupBy);
    }
}
