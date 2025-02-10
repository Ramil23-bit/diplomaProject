package org.telran.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.converter.FavoritCreateConverter;
import org.telran.web.dto.FavoritesCreateDto;
import org.telran.web.dto.FavoritesResponseDto;
import org.telran.web.dto.ProductCreateDto;
import org.telran.web.dto.ProductResponseDto;
import org.telran.web.entity.Favorites;
import org.telran.web.entity.Product;
import org.telran.web.service.FavoritesService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto> converter;

    @GetMapping
    private List<FavoritesResponseDto> getAll(){
        return favoritesService.getAll().stream()
                .map(favorites -> converter.toDto(favorites))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private FavoritesResponseDto create(@RequestBody FavoritesCreateDto favoritesCreateDto) {
        return converter.toDto(favoritesService.create(converter.toEntity(favoritesCreateDto)));
    }

    @DeleteMapping("/{favoriteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(@PathVariable Long favoriteId) {
        favoritesService.deleteById(favoriteId);
    }

}
