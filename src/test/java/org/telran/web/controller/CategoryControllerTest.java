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
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.service.CategoryService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @MockBean
    private CategoryService categoryService;

    @MockBean
    private Converter<Category, CategoryCreateDto, CategoryResponseDto> converter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createCategory() throws Exception {
        CategoryCreateDto createDto = new CategoryCreateDto("Electronics", Collections.emptyList());

        Category category = new Category(1L, "Electronics", Collections.emptyList());
        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "Electronics");

        when(converter.toEntity(createDto)).thenReturn(category);
        when(categoryService.create(category)).thenReturn(category);
        when(converter.toDto(category)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                        .content(asJsonString(createDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void getCategoryById() throws Exception {
        Category category = new Category(1L, "Electronics", Collections.emptyList());
        CategoryResponseDto responseDto = new CategoryResponseDto(1L, "Electronics");

        when(categoryService.getById(anyLong())).thenReturn(category);
        when(converter.toDto(category)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(responseDto)));
    }

    @Test
    void getCategoryByIdWhenIdDoesNotExist() throws Exception {
        Long nonExistentId = 999L;
        when(categoryService.getById(nonExistentId)).thenThrow(BadArgumentsException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
    }

    @Test
    void deleteCategoryById() throws Exception {
        Long categoryId = 1L;
        doNothing().when(categoryService).delete(categoryId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteCategoryByIdWhenIdDoesNotExist() throws Exception {
        Long nonExistentId = 999L;
        doThrow(BadArgumentsException.class).when(categoryService).delete(nonExistentId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
