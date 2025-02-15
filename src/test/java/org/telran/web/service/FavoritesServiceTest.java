package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.*;
import org.telran.web.repository.FavoritesRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 *   Key Features:
 * - Uses `@ExtendWith(MockitoExtension.class)` for Mockito integration.
 * - Mocks `FavoritesRepository` and `UserService` to isolate service logic.
 * - Covers **retrieving and creating favorite items**.
 * - Ensures **correct mapping of users and products**.
 */

@ExtendWith(MockitoExtension.class)
class FavoritesServiceTest {

    @Mock
    private FavoritesRepository favoritesRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FavoritesServiceImpl favoritesService;

    /**
     **Test Case: Retrieve all favorite items for a user.
     **Expected Result: Returns a list of favorite items.
     */
    @Test
    public void getAllFavorites() {
        Favorites favoritesOne = createFavoritesList().get(0);
        Favorites favoritesTwo = createFavoritesList().get(1);
        List<Favorites> favoritesListFromMock = Arrays.asList(favoritesOne, favoritesTwo);

        when(userService.getCurrentUserId()).thenReturn(1L);
        when(favoritesRepository.findAllByUserId(1L)).thenReturn(favoritesListFromMock);

        List<Favorites> favoritesList = favoritesService.getAll();

        assertNotNull(favoritesList);  // List should not be null
        assertEquals(favoritesList.size(), favoritesListFromMock.size());  // Size should match expected
        assertEquals(favoritesList.get(0), favoritesListFromMock.get(0));  // First item should match
        assertEquals(favoritesList.get(1), favoritesListFromMock.get(1));  // Second item should match
    }

    /**
     **Test Case:** Create a new favorite item.
     **Expected Result:** The favorite is successfully created and has a valid ID.
     */
    @Test
    public void createFavorite() {
        Favorites favorites = createFavoritesList().get(0);
        favorites.setId(null);  // New favorite should have no ID initially
        Favorites savedFavorites = createFavoritesList().get(0);
        savedFavorites.setId(1L);  // Expected saved favorite with ID

        when(favoritesRepository.save(favorites)).thenReturn(savedFavorites);
        Favorites createdFavorites = favoritesService.create(favorites);

        assertNotNull(createdFavorites);  // Created favorite should not be null
        assertNotNull(createdFavorites.getId());  // ID should be assigned
        assertEquals(1L, savedFavorites.getId());  // ID should match expected
    }

    /**
     **Helper Method: Creates a list of favorite items for testing.
     **Ensures: The correct mapping of users and products in favorites.
     */
    private List<Favorites> createFavoritesList() {
        return Arrays.asList(
                new Favorites(
                        new User(
                                1L, "Masha", "masha@mail.com", "pass", "111111"
                        ),
                        new Product(
                                1L, "BMW", BigDecimal.valueOf(140000.00), "M5",
                                new Category(1L, "Auto BMW"), new Storage(1L, 1L), BigDecimal.ZERO
                        )),
                new Favorites(
                        new User(
                                2L, "Iulia", "iulia@mail.com", "pass", "111111"
                        ),
                        new Product(
                                2L, "MB", BigDecimal.valueOf(200000.00), "S63AMG",
                                new Category(2L, "Auto MB"), new Storage(1L, 1L), BigDecimal.ZERO
                        )
                )
        );
    }
}