package org.telran.web.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cart_items")
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private Cart cart;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "cart_product_id")
    //private Product product;

    public CartItems(Long id, Long quantity, Cart cart) {
        this.id = id;
        this.quantity = quantity;
        this.cart = cart;
        //this.product = product;
    }

    public CartItems() {
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", cart=" + cart +
                '}';
    }
}
