package org.telran.web.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.dto.*;
import org.telran.web.entity.Product;
import org.telran.web.enums.GroupBy;
import org.telran.web.enums.TimeUnitEnum;
import org.telran.web.service.ReportService;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Retrieves the top 10 most purchased products.
     *
     * @return A list of the top 10 purchased products.
     */
    @GetMapping("/top-purchased")
    public List<ProductReportDto> getTopPurchasedProducts() {
        return reportService.getTop10PurchasedProducts();
    }

    /**
     * Retrieves the top 10 most cancelled products.
     *
     * @return A list of the top 10 cancelled products.
     */
    @GetMapping("/top-cancelled")
    public List<ProductReportDto> getTopCancelledProducts() {
        return reportService.getTop10CancelledProducts();
    }

    /**
     * Retrieves a list of unpaid products older than the specified number of days.
     *
     * @param days The number of days after which unpaid products should be retrieved.
     * @return A list of unpaid products older than the specified number of days.
     */
    @GetMapping("/unpaid-products")
    public List<Product> getUnpaidProducts(@RequestParam int days) {
        return reportService.getUnpaidProductsOlderThan(days);
    }

    /**
     * Retrieves revenue reports for the given time period and grouping criteria.
     *
     * @param n       The number of time units (e.g., 3 days, 2 months).
     * @param unit    The time unit for the report (e.g., HOURS, DAYS, WEEKS, MONTHS, YEARS).
     * @param groupBy The grouping criteria (e.g., category, date, customer).
     * @return A list of revenue reports based on the provided parameters.
     */
    @GetMapping("/revenue")
    public List<RevenueReportDto> getRevenue(
            @RequestParam int n,
            @RequestParam
            @Schema(
                    description = "Allowed values: HOURS, DAYS, WEEKS, MONTHS, YEARS",
                    allowableValues = {"HOURS", "DAYS", "WEEKS", "MONTHS", "YEARS"}
            ) TimeUnitEnum unit,
            @RequestParam
            @Schema(
                    description = "Allowed values: category, date, customer",
                    allowableValues = {"category", "date", "customer"}
            ) String groupBy) {

        return reportService.getRevenueReport(n, unit.toChronoUnit(), GroupBy.fromString(groupBy));
    }
}