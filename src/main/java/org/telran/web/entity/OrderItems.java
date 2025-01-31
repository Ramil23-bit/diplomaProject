package org.telran.web.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private BigDecimal priceAtPurchase;

    @ManyToOne
    @JoinColumn(name = "order_item_product_id", foreignKey = @ForeignKey(name = "FK_ORDER_ITEMS_PRODUCT"))
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDER_ITEMS_ORDER"))
    @JsonIgnore
    private Orders orders;

    // Конструктор с ID (используется, если ID передаётся явно)
    public OrderItems(Long id, Long quantity, BigDecimal priceAtPurchase, Product product, Orders orders) {
        this.id = id;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
        this.product = product;
        this.orders = orders;
    }

    // Конструктор без ID (используется для создания новой записи)
    public OrderItems(Long quantity, BigDecimal priceAtPurchase, Product product, Orders orders) {
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
        this.product = product;
        this.orders = orders;
    }

    public OrderItems() {
        // Пустой конструктор для JPA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", priceAtPurchase=" + priceAtPurchase +
                ", product=" + (product != null ? product.getId() : "null") +
                ", order=" + (orders != null ? orders.getId() : "null") +
                '}';
    }
}
