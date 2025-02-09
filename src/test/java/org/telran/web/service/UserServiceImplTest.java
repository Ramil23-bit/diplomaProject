package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.entity.User;
import org.telran.web.exception.UserNotFoundException;
import org.telran.web.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private FavoritesService favoritesService;
    @Mock
    private CartService cartService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserCreateDto userCreateDto;

    @Test
    public void getAllUserTest(){
        User userOne = new User("Igor", "igor@igor.com", "1234j", "89671329812");
        User userTwo = new User("Oleg", "oleg@oleg.com", "978jkhb", "+79237864121");
        List<User> userFromMock = Arrays.asList(userOne, userTwo);
        Mockito.when(userRepository.findAll()).thenReturn(userFromMock);

        List<User> userList = userService.getAll();

        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals("Oleg", userList.get(1).getUsername());
    }

    @Test
    public void getByIdUserWhenUserExist(){
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getById(userId);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void getByIdUserWhenUserNotExist(){
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId))
                .thenThrow(new UserNotFoundException("User not Found"));

        assertThrows(UserNotFoundException.class, () ->userService.getById(userId));
    }

    @Test
    public void createUserReturnSavedUser(){
        User userExpected = new User("Igor", "igor@igor.com", "1234j", "89671329812");
        Mockito.when(userRepository.save(userExpected))
                .thenReturn(userExpected);

        User userActual = userService.create(userExpected);

        assertNotNull(userActual);
        assertEquals("Igor", userActual.getUsername());
        Mockito.verify(userRepository, times(1)).save(userExpected);
    }

    @Test
    public void updateUserReturnSaveUser() {
        User userExpected = new User("Igor", "igor@igor.com", "1234j", "89671329812");
        userExpected.setId(1L);

        UserCreateDto userActual = new UserCreateDto("Oleg", "oleg@oleg.com", "978523k", "89361234576");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userExpected));

        // ✅ Мокируем passwordEncoder
        when(passwordEncoder.encode(userActual.getPassword())).thenReturn(userActual.getPassword());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setPassword(userActual.getPassword());
            return savedUser;
        });

        User userUpdate = userService.updateUser(1L, userActual);

        assertNotNull(userUpdate);
        assertEquals("Oleg", userUpdate.getUsername());
        assertEquals("oleg@oleg.com", userUpdate.getEmail());
        assertEquals("978523k", userUpdate.getPassword()); // ✅ Теперь пароль обновляется!
        assertEquals("89361234576", userUpdate.getPhoneNumber());
    }

    @Test
    public void updateUserWhenUserNotFound() {
        userCreateDto = new UserCreateDto("Oleg", "oleg@oleg.com", "password", "89361234576");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, ()->{
            userService.updateUser(1L, userCreateDto);
        });

        assertEquals("User with id 1 not found", userNotFoundException.getMessage());
    }

    @Test
    public void deleteUserWhenUserExist() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).existsById(1L); // ✅ Проверяем вызов existsById()
        verify(favoritesService, times(1)).deleteByUser(1L); // ✅ Проверяем вызов deleteByUser()
        verify(cartService, times(1)).deleteByUser(1L); // ✅ cartService теперь доступен!
        verify(userRepository, times(1)).deleteById(1L);
    }
}
