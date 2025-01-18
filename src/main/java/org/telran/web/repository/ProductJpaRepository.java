package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telran.web.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long>{
    @Query("SELECT p FROM Product p WHERE p.discount =:discount")
    List<Product> getAllProductByDiscount(@Param("discount") BigDecimal discount);
    @Query("SELECT min(p.price) FROM Product p")
    List<Product> findAllProductByMinMaxPrice(@Param("price") BigDecimal price);


}
