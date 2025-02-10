package org.telran.web.service;

import org.telran.web.entity.Favorites;
import org.telran.web.entity.User;

import java.util.List;

public interface FavoritesService {

    Favorites create(Favorites favorites);
    List<Favorites> getAll();
    void deleteByUser(Long user);
    void deleteById(Long favoriteId);

}
