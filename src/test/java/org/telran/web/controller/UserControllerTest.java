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
import org.telran.web.dto.UserCreateDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.User;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.service.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private Converter<User, UserCreateDto, UserResponseDto> converter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto("user1", "user1@example.com", "password123", "+1234567890");
        User user = new User(1L, "user1", "user1@example.com", "password123", "+1234567890");
        UserResponseDto responseDto = new UserResponseDto(1L, "user1", "user1@example.com", "+1234567890");

        when(converter.toEntity(createDto)).thenReturn(user);
        when(userService.create(user)).thenReturn(user);
        when(converter.toDto(user)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shop_users")
                        .content(asJsonString(createDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void getUserById() throws Exception {
        User user = new User(1L, "user1", "user1@example.com", "password123", "+1234567890");
        UserResponseDto responseDto = new UserResponseDto(1L, "user1", "user1@example.com", "+1234567890");

        when(userService.getById(1L)).thenReturn(user);
        when(converter.toDto(user)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/shop_users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(responseDto)));
    }

    @Test
    void getUserByIdWhenIdDoesNotExist() throws Exception {
        Long nonExistentId = 1L;
        when(userService.getById(nonExistentId)).thenThrow(BadArgumentsException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/shop_users/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof BadArgumentsException));
    }

    @Test
    void deleteUser() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUserById(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/shop_users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void deleteUserByIdWhenIdDoesNotExist() throws Exception {
        Long nonExistentId = 1L;
        doThrow(BadArgumentsException.class).when(userService).deleteUserById(nonExistentId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/shop_users/" + nonExistentId)
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
