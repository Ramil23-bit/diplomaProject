package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Favorites;
import org.telran.web.repository.FavoritesRepository;

import java.util.List;

@Service
public class FavoritesServiceImpl implements FavoritesService {

    private Logger log = LoggerFactory.getLogger(FavoritesServiceImpl.class);

    @Autowired
    private FavoritesRepository repository;

    @Override
    public Favorites create(Favorites favorites) {
        return repository.save(favorites);
    }

    @Override
    public List<Favorites> getAll() {
        List<Favorites> all = repository.findAll();
        log.info("All{}", all);
        return all;
    }
}
