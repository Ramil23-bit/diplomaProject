package org.telran.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telran.web.dto.FavoritesCreateDto;
import org.telran.web.dto.FavoritesResponseDto;
import org.telran.web.entity.Favorites;
import org.telran.web.service.ProductService;
import org.telran.web.service.UserService;

@Component
public class FavoritCreateConverter implements Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto>{

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserCreateConverter userCreateConverter;

    @Override
    public FavoritesResponseDto toDto(Favorites favorites) {
        return new FavoritesResponseDto(favorites.getId(), userCreateConverter.toDto(favorites.getUser()), favorites.getProduct());
    }

    @Override
    public Favorites toEntity(FavoritesCreateDto dto) {
        Favorites favorites = new Favorites(productService.getById(dto.getProductId()));
        favorites.setUser(userService.getById(userService.getCurrentUserId()));
        return favorites;
    }
}