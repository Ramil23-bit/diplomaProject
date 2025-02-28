package org.telran.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.SecurityConfig;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CategoryCreateDto;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.entity.Category;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.security.JwtService;
import org.telran.web.service.CategoryService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CategoryController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private Converter<Category, CategoryCreateDto, CategoryResponseDto> categoryConverter;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtService jwtService;

    @InjectMocks
    private CategoryController categoryController;

    // Set test profile for isolated configuration
    static {
        System.setProperty("spring.profiles.active", "test");
    }

    /**
     **Test Case:** User retrieves all categories
     **Expected Result:** HTTP 200 (OK) with an empty list (if no categories exist)
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllCategoriesAsUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())  // ✅ HTTP 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())  // ✅ Ensures the response is an array
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());  // ✅ Ensures the array is empty
    }

    /**
     **Test Case:** Admin retrieves a category by ID
     **Expected Result:** HTTP 200 (OK) with valid category details
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getCategoryByIdAsAdminTest() throws Exception {
        // Mock category data
        Category category = new Category(1L, "Category Name");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "Category Name");

        // Mock service behavior
        when(categoryService.getById(1L)).thenReturn(category);
        when(categoryConverter.toDto(any(Category.class))).thenReturn(categoryResponseDto);

        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())  // ✅ HTTP 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))  // ✅ ID must be 1
                .andExpect(MockMvcResultMatchers.jsonPath("$.category_title").value("Category Name"));  // ✅ Title must match
    }

    /**
     **Test Case:** Admin deletes a category by ID
     **Expected Result:** HTTP 200 (OK) with no content
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteCategoryByIdAsAdminTest() throws Exception {
        // Execute request and validate response
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());  // ✅ HTTP 200 OK
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