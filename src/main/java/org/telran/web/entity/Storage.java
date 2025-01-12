package org.telran.web.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "storage")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL)
    private List<Product> products;

    public Storage() {
        //
    }

    public Storage(Long id, Long amount, List<Product> products) {
        this.id = id;
        this.amount = amount;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
