package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.FavoritesCreateDto;
import org.telran.web.dto.FavoritesResponseDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.Favorites;
import org.telran.web.entity.Product;
import org.telran.web.entity.User;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.FavoritesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = FavoritesController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import({TestSecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FavoritesControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean
    private FavoritesService favoritesService;
    @MockBean
    private Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto> converter;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllFavoritesTest() throws Exception {
        // Подготовка данных
        User user = new User("username", "email@example.com", "123456", "+1234567890");
        Product product = new Product();
        Favorites favorite = new Favorites(1L, user, product);

        // Настройка мока для favoritesService
        when(favoritesService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/favorites")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteFavoriteTest() throws Exception {
        Long favoriteId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/favorites/{favoriteId}", favoriteId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(favoritesService, times(1)).deleteById(favoriteId);
    }

    @Test
    @WithAnonymousUser
    void getAllFavoritesAsAnonymousTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/favorites")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void addProductToFavoritesAsAnonymousTest() throws Exception {
        FavoritesCreateDto favoritesCreateDto = new FavoritesCreateDto(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/favorites")
                        .content(asJsonString(favoritesCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    void deleteFavoriteAsAnonymousTest() throws Exception {
        Long favoriteId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/favorites/{favoriteId}", favoriteId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


