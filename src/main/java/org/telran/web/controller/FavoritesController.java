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

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    @Qualifier("favoritCreateConverter")
    private Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto> converter;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<FavoritesResponseDto> getAll() {
        return favoritesService.getAll().stream()
                .map(favorites -> converter.toDto(favorites))
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritesResponseDto create(@RequestBody FavoritesCreateDto favoritesCreateDto) {
        return converter.toDto(favoritesService.create(converter.toEntity(favoritesCreateDto)));
    }

    @DeleteMapping("/{favoriteId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(@PathVariable Long favoriteId) {
        favoritesService.deleteById(favoriteId);
    }

}
