package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Cart;

import java.util.Optional;

@Repository
public interface CartJpaRepository extends JpaRepository<Cart, Long> {

    @Modifying
    @Query("DELETE FROM Favorites f WHERE f.user.id = :userId")
    void deleteByUser(Long userId);

    Optional<Cart> findByUserId(Long currentUserId);
}
