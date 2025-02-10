package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Favorites;
import org.telran.web.entity.User;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    @Modifying
    @Query("DELETE FROM Favorites f WHERE f.user.id = :userId")
    void deleteByUser(Long userId);

    List<Favorites> findAllByUserId(Long userId);

    void deleteById(Long favoriteId);
}
