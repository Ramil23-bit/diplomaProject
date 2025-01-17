package org.telran.web.service;

import org.telran.web.entity.Favorites;

import java.util.List;

public interface FavoritesService {

    Favorites create(Favorites favorites);
    List<Favorites> getAll();

}
