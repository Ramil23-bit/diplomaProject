package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Controller for managing favorite products.
 * Provides endpoints to add, retrieve, and remove favorites.
 */
@RestController
@RequestMapping("/api/v1/favorites")
public class FavoritesController {

    private static final Logger logger = LoggerFactory.getLogger(FavoritesController.class);

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto> converter;

    /**
     * Retrieves all favorite products of the current user.
     *
     * @return List of favorite response DTOs.
     */
    @Operation(summary = "Get all favorites", description = "Retrieves a list of all favorite products for the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorites successfully retrieved")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<FavoritesResponseDto> getAll() {
        logger.info("Fetching all favorites for current user");
        List<FavoritesResponseDto> favorites = favoritesService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        logger.info("Total favorites retrieved: {}", favorites.size());
        return favorites;
    }

    /**
     * Adds a product to the user's favorites.
     *
     * @param favoritesCreateDto The DTO containing favorite product details.
     * @return The created favorite response DTO.
     */
    @Operation(summary = "Add a product to favorites", description = "Adds a product to the user's favorite list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Favorite successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritesResponseDto create(@RequestBody FavoritesCreateDto favoritesCreateDto) {
        logger.info("Received request to add product to favorites: {}", favoritesCreateDto);
        FavoritesResponseDto response = converter.toDto(favoritesService.create(converter.toEntity(favoritesCreateDto)));
        logger.info("Favorite added successfully with ID: {}", response.getFavoriteId());
        return response;
    }

    /**
     * Removes a product from the user's favorites.
     *
     * @param favoriteId The ID of the favorite product to remove.
     */
    @Operation(summary = "Delete favorite by ID", description = "Removes a specific product from the favorites list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Favorite successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Favorite not found")
    })
    @DeleteMapping("/{favoriteId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(@PathVariable Long favoriteId) {
        logger.info("Request to delete favorite with ID: {}", favoriteId);
        favoritesService.deleteById(favoriteId);
        logger.info("Favorite with ID {} successfully deleted", favoriteId);
    }
}
