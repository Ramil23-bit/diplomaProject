package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Favorites;
import org.telran.web.entity.User;
import org.telran.web.repository.FavoritesRepository;

import java.util.List;

@Service
public class FavoritesServiceImpl implements FavoritesService {

    private Logger log = LoggerFactory.getLogger(FavoritesServiceImpl.class);

    @Autowired
    private FavoritesRepository repository;

    @Lazy
    @Autowired
    private UserService userService;

    @Override
    public Favorites create(Favorites favorites) {
        return repository.save(favorites);
    }

    @Override
    public List<Favorites> getAll() {
        Long userId = userService.getCurrentUserId();
        List<Favorites> all = repository.findAllByUserId(userId);
        log.info("All{}", all);
        return all;
    }

    @Override
    public void deleteByUser(Long user) {
        repository.deleteByUser(user);
    }

    @Override
    public void deleteById(Long favoriteId) {
        repository.deleteById(favoriteId);
    }
}
