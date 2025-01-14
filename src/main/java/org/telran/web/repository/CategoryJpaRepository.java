package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Category;

import java.util.List;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();

    @Modifying
    @Query("UPDATE Category i SET i.categoryTitle = :newTitle WHERE i.id = :id")
    int updateTitle(@Param("id") Long id, @Param("newTitle") String newTitle);
}
