package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Favorites;
import org.telran.web.entity.User;
import org.telran.web.exception.FavoritesNotFoundException;
import org.telran.web.repository.FavoritesRepository;

import java.util.List;

/**
 * Implementation of FavoritesService.
 * Handles business logic for managing favorite items.
 */
@Service
public class FavoritesServiceImpl implements FavoritesService {

    private Logger log = LoggerFactory.getLogger(FavoritesServiceImpl.class);

    @Autowired
    private FavoritesRepository repository;

    @Lazy
    @Autowired
    private UserService userService;

    /**
     * Creates a new favorite entry and saves it in the repository.
     *
     * @param favorites Favorites entity containing favorite details.
     * @return The created Favorites entity.
     */
    @Override
    public Favorites create(Favorites favorites) {
        return repository.save(favorites);
    }

    /**
     * Retrieves all favorite entries associated with the current user.
     *
     * @return List of Favorites entities belonging to the current user.
     */
    @Override
    public List<Favorites> getAll() {
        Long userId = userService.getCurrentUserId();
        List<Favorites> all = repository.findAllByUserId(userId);
        log.info("All{}", all);
        return all;
    }

    /**
     * Deletes all favorite entries associated with a specific user.
     *
     * @param user ID of the user whose favorite entries should be deleted.
     */
    @Override
    public void deleteByUser(Long user) {
        repository.deleteByUser(user);
    }

    /**
     * Deletes a favorite entry by its ID.
     * Ensures that only the owner of the favorite entry can delete it.
     *
     * @param favoriteId ID of the favorite entry to delete.
     * @throws FavoritesNotFoundException if the favorite entry is not found or does not belong to the current user.
     */
    @Override
    public void deleteById(Long favoriteId) {
        Favorites favorites = repository.findById(favoriteId)
                .orElseThrow(() -> new FavoritesNotFoundException("Not found"));
        if (userService.getCurrentUserId().equals(favorites.getUser().getId())) {
            repository.deleteById(favoriteId);
        } else {
            throw new FavoritesNotFoundException("Not found");
        }
    }
}
