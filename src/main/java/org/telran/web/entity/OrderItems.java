package org.telran.web.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    private BigDecimal priceAtPurchase;

//    @ManyToOne
//    @JoinColumn(name = "order_item_order_id")
//    private Order order;

    @ManyToOne
    @JoinColumn(name = "order_item_product_id", foreignKey = @ForeignKey(name = "FK_ORDER_ITEMS_PRODUCT"))
    private Product product;

    public OrderItems(Long id, Long quantity, BigDecimal priceAtPurchase, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
        this.product = product;
    }

    public OrderItems() {
        //
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

    @Override
    public String toString() {
        return "OrderItems{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", priceAtPurchase=" + priceAtPurchase +
                ", product=" + product +
                '}';
    }
}
