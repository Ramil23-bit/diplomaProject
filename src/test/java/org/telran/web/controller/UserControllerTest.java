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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.telran.web.configuration.SecurityConfig;
import org.telran.web.configuration.TestSecurityConfig;
import org.telran.web.converter.Converter;
import org.telran.web.converter.UserCreateConverter;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.User;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.security.JwtAuthenticationFilter;
import org.telran.web.security.JwtService;
import org.telran.web.service.UserService;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 *   Key Features:
 * - Uses `MockMvc` to simulate HTTP requests.
 * - Tests role-based access control (`ADMIN`).
 * - Mocks `UserService` and `Converter` to isolate controller behavior.
 * - Verifies proper HTTP response codes and JSON structure.
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@Import({SecurityConfig.class, UserCreateConverter.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private Converter<User, UserCreateDto, UserResponseDto> converter;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtService jwtService;

    static {
        System.setProperty("spring.profiles.active", "test");
    }

    /**
     **Test Case:** Register a new user
     **Expected Result:** HTTP 201 (Created) with correct user details
     */
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto("user1", "user1@example.com", "password123", "+1234567890");
        User user = new User(1L, "user1", "user1@example.com", "password123", "+1234567890");
        UserResponseDto responseDto = new UserResponseDto(1L, "user1", "user1@example.com", "+1234567890");

        when(converter.toEntity(any(UserCreateDto.class))).thenReturn(user);
        when(userService.create(any(User.class))).thenReturn(user);
        when(converter.toDto(any(User.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .content(asJsonString(createDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())  // HTTP 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(responseDto.getId()))  // ID matches
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(responseDto.getEmail()));  // Email matches
    }

    /**
     **Test Case:** Get user by ID
     **Expected Result:** HTTP 200 (OK) with correct user details
     */
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getUserById() throws Exception {
        User user = new User(1L, "user1", "user1@example.com", "password123", "+1234567890");
        UserResponseDto responseDto = new UserResponseDto(1L, "user1", "user1@example.com", "+1234567890");

        when(userService.getById(1L)).thenReturn(user);
        when(converter.toDto(user)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())  // HTTP 200 OK
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(responseDto)));  // JSON matches expected response
    }

    /**
     **Test Case:** Get user by ID when user does not exist
     **Expected Result:** HTTP 400 (Bad Request) with an appropriate error message
     */
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getUserByIdWhenIdDoesNotExist() throws Exception {
        Long nonExistentId = 1L;
        when(userService.getById(nonExistentId)).thenThrow(new BadArgumentsException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  // HTTP 400 Bad Request
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));  // Exception matches
    }

    /**
     **Utility Method:** Converts an object to a JSON string
     **Used to convert DTOs to JSON for test requests**
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}