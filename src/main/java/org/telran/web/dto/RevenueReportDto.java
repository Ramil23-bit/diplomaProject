package org.telran.web.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing revenue reports.
 * This class is used to store and transfer revenue data grouped by a specified time period.
 */
public class RevenueReportDto {

    /**
     * Represents the time period for which the revenue is calculated.
     * The value corresponds to the grouping criteria (e.g., hour, day, week, month).
     */
    private Integer period;

    /**
     * Represents the total revenue for the specified time period.
     */
    private BigDecimal revenue;

    /**
     * Constructs a new RevenueReportDto with the specified period and revenue amount.
     *
     * @param period  The time period for which the revenue is calculated (e.g., hour, day, week, month).
     * @param revenue The total revenue for the specified period.
     */
    public RevenueReportDto(Integer period, BigDecimal revenue) {
        this.period = period;
        this.revenue = revenue;
    }

    /**
     * Retrieves the time period of the revenue report.
     *
     * @return The period value (e.g., hour, day, week, month).
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * Sets the time period of the revenue report.
     *
     * @param period The period value (e.g., hour, day, week, month).
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * Retrieves the total revenue for the specified period.
     *
     * @return The total revenue amount.
     */
    public BigDecimal getRevenue() {
        return revenue;
    }

    /**
     * Sets the total revenue for the specified period.
     *
     * @param revenue The total revenue amount.
     */
    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}