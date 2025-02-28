package org.telran.web.entity;

import jakarta.persistence.*;
import org.telran.web.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_product")
    private String nameProduct;
    @JoinColumn(name = "total_profit")
    private BigDecimal totalProfit;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Long amount;
    @Column(name = "create_date")
    private LocalDateTime createDate;

    public Report() {
    }

    public Report(Long id, String nameProduct, BigDecimal totalProfit) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.totalProfit = totalProfit;
    }

    public Report(Long id, String nameProduct, BigDecimal totalProfit, Long amount, LocalDateTime createDate) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.totalProfit = totalProfit;
        this.amount = amount;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
