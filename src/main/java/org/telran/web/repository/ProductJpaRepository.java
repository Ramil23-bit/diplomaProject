package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long>{
    @Modifying
    @Query("SELECT p.productInfo FROM Product p WHERE p.discount =:discount")
    List<Product> getAllProductByDiscount(@Param("discount") BigDecimal discount);


}
