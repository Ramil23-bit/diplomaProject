package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.FavoritesCreateDto;
import org.telran.web.dto.FavoritesResponseDto;
import org.telran.web.entity.Favorites;
import org.telran.web.service.ProductService;
import org.telran.web.service.UserService;

/**
 * Converter class for transforming Favorites entities to DTOs and vice versa.
 * Handles the conversion between Favorites, FavoritesCreateDto, and FavoritesResponseDto.
 */
@Component
public class FavoritCreateConverter implements Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto> {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserCreateConverter userCreateConverter;

    /**
     * Converts a Favorites entity to a FavoritesResponseDto.
     *
     * @param favorites The Favorites entity to convert.
     * @return A FavoritesResponseDto representing the favorite item.
     */
    @Override
    public FavoritesResponseDto toDto(Favorites favorites) {
        return new FavoritesResponseDto(favorites.getId(), userCreateConverter.toDto(favorites.getUser()), favorites.getProduct());
    }

    /**
     * Converts a FavoritesCreateDto to a Favorites entity.
     *
     * @param dto The DTO containing favorite creation data.
     * @return The created Favorites entity.
     */
    @Override
    public Favorites toEntity(FavoritesCreateDto dto) {
        Favorites favorites = new Favorites(productService.getById(dto.getProductId()));
        favorites.setUser(userService.getById(userService.getCurrentUserId()));
        return favorites;
    }
}
