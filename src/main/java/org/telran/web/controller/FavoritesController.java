package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.FavoritesCreateDto;
import org.telran.web.dto.FavoritesResponseDto;
import org.telran.web.entity.Favorites;
import org.telran.web.service.FavoritesService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing favorite items.
 * Provides endpoints for retrieving, creating, and deleting favorites.
 */
@RestController
@RequestMapping("/api/v1/favorites")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    @Qualifier("favoritCreateConverter")
    private Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto> converter;

    /**
     * Retrieves all favorite items.
     *
     * @return List of FavoritesResponseDto representing all favorites.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<FavoritesResponseDto> getAll() {
        return favoritesService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new favorite item.
     *
     * @param favoritesCreateDto DTO containing the favorite details.
     * @return FavoritesResponseDto representing the created favorite.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritesResponseDto create(@RequestBody FavoritesCreateDto favoritesCreateDto) {
        return converter.toDto(favoritesService.create(converter.toEntity(favoritesCreateDto)));
    }

    /**
     * Deletes a favorite item by its ID.
     *
     * @param favoriteId ID of the favorite to delete.
     */
    @DeleteMapping("/{favoriteId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(@PathVariable Long favoriteId) {
        favoritesService.deleteById(favoriteId);
    }

}
