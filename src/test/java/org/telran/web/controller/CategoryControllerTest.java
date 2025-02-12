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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.SecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.CategoryService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CategoryController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@ActiveProfiles("test")
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private Converter<Category, CategoryCreateDto, CategoryResponseDto> categoryConverter;

    @Autowired
    private MockMvc mockMvc;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategoryTest() throws Exception {
        CategoryCreateDto categoryCreateDto = new CategoryCreateDto("Electronics", null);

        Category category = new Category(1L, "Electronics");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Electronics");

        when(categoryConverter.toEntity(any(CategoryCreateDto.class))).thenReturn(category);
        when(categoryService.create(any(Category.class))).thenReturn(category);
        when(categoryConverter.toDto(any(Category.class))).thenReturn(categoryResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                        .content(asJsonString(categoryCreateDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryTitle").value("Electronics"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllCategoriesTest() throws Exception {
        Category category = new Category(1L, "Electronics");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Electronics");

        when(categoryService.getAll()).thenReturn(List.of(category));
        when(categoryConverter.toDto(any(Category.class))).thenReturn(categoryResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryTitle").value("Electronics"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCategoryByIdTest() throws Exception {
        Category category = new Category(1L, "Electronics");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Electronics");

        when(categoryService.getById(1L)).thenReturn(category);
        when(categoryConverter.toDto(any(Category.class))).thenReturn(categoryResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryTitle").value("Electronics"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteCategoryByIdTest() throws Exception {
        doNothing().when(categoryService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void editCategoryTitleTest() throws Exception {
        Category category = new Category(1L, "Electronics");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "NewTitle");

        when(categoryService.getById(1L)).thenReturn(category);
        when(categoryConverter.toDto(any(Category.class))).thenReturn(categoryResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/edit/title/1")
                        .param("newTitle", "NewTitle"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryTitle").value("NewTitle"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addProductToCategoryTest() throws Exception {
        Category category = new Category(1L, "Electronics");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Electronics");

        when(categoryService.editListOfProductsAddProduct(1L, 100L)).thenReturn(category);
        when(categoryConverter.toDto(any(Category.class))).thenReturn(categoryResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/edit/add_product/1")
                        .param("newProduct", "100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryTitle").value("Electronics"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
