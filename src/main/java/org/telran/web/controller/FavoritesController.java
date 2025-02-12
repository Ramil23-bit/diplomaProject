package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * Provides endpoints to add, retrieve, and delete favorites.
 */
@RestController
@RequestMapping("/api/v1/favorites")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto> converter;

    /**
     * Retrieves all favorite products of the current user.
     *
     * @return List of favorite products response DTOs.
     */
    @Operation(summary = "Get all favorite products", description = "Retrieves a list of all favorite products for the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorites successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<FavoritesResponseDto> getAll() {
        return favoritesService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Adds a product to the user's favorites.
     *
     * @param favoritesCreateDto The DTO containing favorite product details.
     * @return The created favorite response DTO.
     */
    @Operation(summary = "Add product to favorites", description = "Adds a product to the user's favorite list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully added to favorites"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritesResponseDto create(@RequestBody FavoritesCreateDto favoritesCreateDto) {
        return converter.toDto(favoritesService.create(converter.toEntity(favoritesCreateDto)));
    }

    /**
     * Deletes a product from the user's favorites by its ID.
     *
     * @param favoriteId The ID of the favorite product to delete.
     */
    @Operation(summary = "Delete favorite product by ID", description = "Removes a specific product from the user's favorite list by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Favorite successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Favorite not found")
    })
    @DeleteMapping("/{favoriteId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(@PathVariable Long favoriteId) {
        favoritesService.deleteById(favoriteId);
    }
}
