package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long>{
    @Query("SELECT p FROM Product p WHERE p.discount =:discount")
    List<Product> getAllProductByDiscount(@Param("discount") BigDecimal discount);


    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findAllProductByMinMaxPrice(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.categoryTitle =:categoryTitle")
    List<Product> findAllProductByCategoryTitle(@Param("categoryTitle") String categoryTitle);

    @Query("SELECT p FROM Product p WHERE p.productTitle = :name")
    Optional<Product> findByName(String name);
}
