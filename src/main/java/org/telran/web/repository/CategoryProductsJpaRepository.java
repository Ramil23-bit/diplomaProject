package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.CategoryProducts;

@Repository
public interface CategoryProductsJpaRepository extends JpaRepository<CategoryProducts, Long> {
}
