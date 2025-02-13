//package org.telran.web.controller;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.telran.web.converter.Converter;
//import org.telran.web.dto.StorageCreateDto;
//import org.telran.web.dto.StorageResponseDto;
//import org.telran.web.entity.Storage;
//import org.telran.web.security.JwtService;
//import org.telran.web.service.StorageService;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.mockito.ArgumentMatchers.any;
//
//@WebMvcTest(StorageController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class StorageControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private StorageService storageService;
//
//    @MockBean
//    private Converter storageConverter;
//
//    @MockBean
//    private JwtService jwtService;
//
//    @BeforeEach
//    void setup() {
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        when(jwtService.generateToken(any())).thenReturn("mock-token");
//        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
//    }
//
//    @Test
//    void testGetAllStorage() throws Exception {
//        Storage mockStorage = new Storage(1L, 100L);
//        StorageResponseDto mockResponse = new StorageResponseDto(1L, 100L);
//
//        when(storageService.getAllStorage()).thenReturn(List.of(mockStorage));
//        when(storageConverter.toDto(any(Storage.class))).thenReturn(mockResponse);
//
//        mockMvc.perform(get("/api/v1/storage"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].amount", is(100)));
//    }
//
//
//    @Test
//    void testGetStorageById() throws Exception {
//        Storage mockStorage = new Storage(1L, 100L);
//        StorageResponseDto mockResponse = new StorageResponseDto(1L, 100L);
//
//        when(storageService.getByIdStorage(1L)).thenReturn(mockStorage);
//        when(storageConverter.toDto(any(Storage.class))).thenReturn(mockResponse);
//
//        mockMvc.perform(get("/api/v1/storage/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.amount", is(100)));
//    }
//
//
//    @Test
//    void testCreateStorage() throws Exception {
//        StorageCreateDto createDto = new StorageCreateDto(100L);
//        Storage mockStorage = new Storage(1L, 100L);
//        StorageResponseDto mockResponse = new StorageResponseDto(1L, 100L);
//
//        when(storageConverter.toEntity(any(StorageCreateDto.class))).thenReturn(mockStorage);
//        when(storageService.createStorage(any(Storage.class))).thenReturn(mockStorage);
//        when(storageConverter.toDto(any(Storage.class))).thenReturn(mockResponse);
//
//        mockMvc.perform(post("/api/v1/storage")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.amount", is(100)));
//    }
//
//}

