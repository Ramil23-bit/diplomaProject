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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.SecurityConfig;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.FavoritesCreateDto;
import org.telran.web.dto.FavoritesResponseDto;
import org.telran.web.entity.Favorites;
import org.telran.web.entity.Product;
import org.telran.web.entity.User;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.security.JwtService;
import org.telran.web.service.FavoritesService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = FavoritesController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import({SecurityConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FavoritesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoritesService favoritesService;

    @MockBean
    private Converter<Favorites, FavoritesCreateDto, FavoritesResponseDto> converter;

    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtService jwtService;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    /**
     **Test Case:** User retrieves all favorite products
     **Expected Result:** HTTP 200 (OK) with an empty list if no favorites exist
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllFavoritesTest() throws Exception {
        User user = new User("username", "email@example.com", "123456", "+1234567890");
        Product product = new Product();
        Favorites favorite = new Favorites(1L, user, product);

        when(favoritesService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/favorites")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    /**
     **Test Case:** User deletes a product from favorites
     **Expected Result:** HTTP 204 (No Content)
     */
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

    /**
     **Test Case:** Anonymous user attempts to retrieve all favorite products
     **Expected Result:** HTTP 401 (Unauthorized)
     */
    @Test
    @WithAnonymousUser
    void getAllFavoritesAsAnonymousTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/favorites")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    /**
     **Test Case:** Anonymous user attempts to add a product to favorites
     **Expected Result:** HTTP 401 (Unauthorized)
     */
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

    /**
     * Utility method to convert objects to JSON string.
     * @param obj Object to be converted
     * @return JSON string representation
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
