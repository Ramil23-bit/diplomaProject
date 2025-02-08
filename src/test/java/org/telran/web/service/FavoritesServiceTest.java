package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telran.web.entity.*;
import org.telran.web.repository.FavoritesRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoritesServiceTest {

    @Mock
    private FavoritesRepository favoritesRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FavoritesServiceImpl favoritesService;

    @Test
    public void getAllFavorites() {
        Favorites favoritesOne = createFavoritesList().get(0);
        Favorites favoritesTwo = createFavoritesList().get(1);
        List<Favorites> favoritesListFromMock = Arrays.asList(favoritesOne, favoritesTwo);

        when(userService.getCurrentUserId()).thenReturn(1L);

        when(favoritesRepository.findAll()).thenReturn(favoritesListFromMock);
        List<Favorites> favoritesList = favoritesService.getAll();

        assertNotNull(favoritesList);
        assertEquals(favoritesList.size(), favoritesListFromMock.size());
        assertEquals(favoritesList.get(0), favoritesListFromMock.get(0));
        assertEquals(favoritesList.get(1), favoritesListFromMock.get(1));
    }

    @Test
    public void createFavorite() {
        Favorites favorites = createFavoritesList().get(0);
        favorites.setId(null);
        Favorites savedFavorites = createFavoritesList().get(0);
        savedFavorites.setId(1L);

        when(favoritesRepository.save(favorites)).thenReturn(savedFavorites);
        Favorites createdFavorites = favoritesService.create(favorites);

        assertNotNull(createdFavorites);
        assertNotNull(createdFavorites.getId());
        assertEquals( 1L, savedFavorites.getId());
    }

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