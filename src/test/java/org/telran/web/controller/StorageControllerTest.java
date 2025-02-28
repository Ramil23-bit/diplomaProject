package org.telran.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.telran.web.configuration.SecurityConfig;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.converter.StorageCreateConverter;
import org.telran.web.dto.StorageCreateDto;
import org.telran.web.dto.StorageResponseDto;
import org.telran.web.entity.Storage;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.security.JwtService;
import org.telran.web.service.StorageService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

/**
 *   Key Features:
 * - Uses `MockMvc` to simulate HTTP requests.
 * - Tests role-based access control (`ADMIN`, `USER`).
 * - Verifies responses using JSON assertions.
 * - Mocks `StorageService` and `Converter` to isolate controller behavior.
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = StorageController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import({SecurityConfig.class, StorageCreateConverter.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;
    @MockBean
    private Converter<Storage, StorageCreateDto, StorageResponseDto> storageConverter;
    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtService jwtService;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    /**
     **Test Case:** Retrieve all storage items
     **Expected Result:** HTTP 200 (OK) with a list of storage items
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllStorage() throws Exception {
        Storage mockStorage = new Storage(1L, 100L);
        StorageResponseDto mockResponse = new StorageResponseDto(1L, 100L);

        when(storageService.getAllStorage()).thenReturn(List.of(mockStorage));
        when(storageConverter.toDto(mockStorage)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/storage"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].amount", is(100)));
    }

    /**
     **Test Case:** Retrieve a storage item by ID
     **Expected Result:** HTTP 200 (OK) with valid storage item details
     */
    @Test
    @WithMockUser(roles = "USER")
    void testGetStorageById() throws Exception {
        Storage mockStorage = new Storage(1L, 100L);
        StorageResponseDto mockResponse = new StorageResponseDto(1L, 100L);

        when(storageService.getByIdStorage(1L)).thenReturn(mockStorage);
        when(storageConverter.toDto(any(Storage.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/storage/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.amount", is(100)));
    }
}