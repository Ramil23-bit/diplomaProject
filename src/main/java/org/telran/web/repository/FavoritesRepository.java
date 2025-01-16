package org.telran.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telran.web.entity.Favorites;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    //
}
