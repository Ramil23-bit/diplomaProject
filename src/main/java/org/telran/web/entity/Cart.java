package org.telran.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    @MapsId
    private User user;
    @OneToMany
    @JoinColumn(name = "cart_id")
    private List<CartItems> cartItemsList = new ArrayList<>();

    public Cart(Long id, User user, List<CartItems> cartItemsList) {
        this.id = id;
        this.user = user;
        this.cartItemsList = cartItemsList;
    }

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItems> getCartItemsList() {
        return cartItemsList;
    }

    public void setCartItemsList(List<CartItems> cartItemsList) {
        this.cartItemsList = cartItemsList;
    }


}
