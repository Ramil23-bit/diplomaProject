package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.repository.CategoryJpaRepository;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.service.CategoryService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CategoryController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;
    @MockBean
    private Converter<Category, CategoryCreateDto, CategoryResponseDto> categoryConverter;

    // Мокируем репозиторий
    @MockBean
    private CategoryJpaRepository categoryJpaRepository;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setup() {
        // Мокируем сохранение категорий в репозитории
        Category category1 = new Category("Electronics");
        Category category2 = new Category("Books");

        when(categoryJpaRepository.save(any(Category.class))).thenReturn(category1, category2);
        when(categoryJpaRepository.findAll()).thenReturn(List.of(category1, category2));
        when(categoryJpaRepository.findById(1L)).thenReturn(Optional.of(category1));
    }

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllCategoriesAsUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getCategoryByIdAsAdminTest() throws Exception {
        Category category = new Category(1L, "Category Name");

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Category Name");

        when(categoryService.getById(1L)).thenReturn(category);
        when(categoryConverter.toDto(any(Category.class))).thenReturn(categoryResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category_title").value("Category Name")); // Проверка на category_title
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteCategoryByIdAsAdminTest() throws Exception {
        // Выполнение запроса
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


