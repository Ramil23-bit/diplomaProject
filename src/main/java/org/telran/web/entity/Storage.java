package org.telran.web.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "storage")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL)
    private List<Product> products;

    @Column(name = "amount")
    private Long amount;

    public Storage(Long id, List<Product> products, Long amount) {
        this.id = id;
        this.products = products;
        this.amount = amount;
    }

    public Storage() {
        //
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
