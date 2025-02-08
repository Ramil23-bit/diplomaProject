package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.converter.Converter;
import org.telran.web.dto.FavoritesCreateDto;
import org.telran.web.dto.FavoritesResponseDto;
import org.telran.web.entity.Favorites;
import org.telran.web.service.FavoritesService;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(FavoritesController.class)
public class FavoritesControllerTest {
    @MockBean
    private FavoritesService favoritesService;
    @MockBean
    private Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto> converter;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllTest() throws Exception {
        Favorites favorites = new Favorites(1L);
        when(favoritesService.getAll())
                .thenReturn(List.of(favorites));
        when(converter.toDto(favorites))
                .thenReturn(new FavoritesResponseDto(favorites.getId(), null, null));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(new FavoritesResponseDto(
                        favorites.getId(), null, null)))));
    }

    @Test
    void createTest() throws Exception {
        FavoritesCreateDto favoritesCreateDto = new FavoritesCreateDto(1L, 1L);
        Favorites favorites = new Favorites(1L, null, null);
        Favorites newFavorites = new Favorites(favorites.getId());

        when(converter.toEntity(favoritesCreateDto))
                .thenReturn(favorites);
        when(favoritesService.create(favorites))
                .thenReturn(newFavorites);
        when(converter.toDto(newFavorites))
                .thenReturn(new FavoritesResponseDto(newFavorites.getId(), null, null));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/favorites")
                        .content(asJsonString(favoritesCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
