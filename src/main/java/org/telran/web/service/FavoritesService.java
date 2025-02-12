package org.telran.web.service;

import org.telran.web.entity.Favorites;
import org.telran.web.entity.User;

import java.util.List;

/**
 * Service interface for managing favorite items.
 * Provides methods for creating, retrieving, and deleting favorites.
 */
public interface FavoritesService {

    /**
     * Creates a new favorite entry.
     *
     * @param favorites Favorites entity containing favorite details.
     * @return The created Favorites entity.
     */
    Favorites create(Favorites favorites);

    /**
     * Retrieves all favorite entries.
     *
     * @return List of all Favorites entities.
     */
    List<Favorites> getAll();

    /**
     * Deletes favorite entries associated with a specific user.
     *
     * @param user ID of the user whose favorite entries should be deleted.
     */
    void deleteByUser(Long user);

    /**
     * Deletes a favorite entry by its ID.
     *
     * @param favoriteId ID of the favorite entry to delete.
     */
    void deleteById(Long favoriteId);
}
