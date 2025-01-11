package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Category;

import java.util.List;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();
}
