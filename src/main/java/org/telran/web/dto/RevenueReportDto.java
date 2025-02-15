package org.telran.web.dto;

import java.math.BigDecimal;

public class RevenueReportDto {

    private Integer period;
    private BigDecimal revenue;

    public RevenueReportDto(Integer period, BigDecimal revenue) {
        this.period = period;
        this.revenue = revenue;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
