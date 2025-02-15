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

/**
 *   Key Features:
 * - Uses `@ExtendWith(MockitoExtension.class)` to enable Mockito for unit testing.
 * - Mocks dependencies like `UserRepository`, `PasswordEncoder`, `FavoritesService`, and `CartService`.
 * - Ensures **proper user retrieval, update, and deletion logic**.
 * - Validates correct **exception handling** for `UserNotFoundException`.
 */

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

    /**
     **Test Case:** Retrieve all users.
     **Expected Result:** Returns a list of users.
     */
    @Test
    public void getAllUserTest() {
        User userOne = new User("Igor", "igor@igor.com", "1234j", "89671329812");
        User userTwo = new User("Oleg", "oleg@oleg.com", "978jkhb", "+79237864121");
        List<User> userFromMock = Arrays.asList(userOne, userTwo);
        Mockito.when(userRepository.findAll()).thenReturn(userFromMock);

        List<User> userList = userService.getAll();

        assertNotNull(userList);
        assertEquals(2, userList.size(), "List size must be 2");
        assertEquals("Oleg", userList.get(1).getUsername(), "Second user must be Oleg");
    }

    /**
     **Test Case:** Retrieve a user by ID when the user exists.
     **Expected Result:** Returns the correct user entity.
     */
    @Test
    public void getByIdUserWhenUserExist() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getById(userId);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser, "User must match the expected result");
    }

    /**
     **Test Case:** Attempt to retrieve a non-existing user by ID.
     **Expected Result:** Throws `UserNotFoundException`.
     */
    @Test
    public void getByIdUserWhenUserNotExist() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId))
                .thenThrow(new UserNotFoundException("User not Found"));

        assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
    }

    /**
     **Test Case:** Create a new user.
     **Expected Result:** User is successfully saved and returned.
     */
    @Test
    public void createUserReturnSavedUser() {
        User userExpected = new User("Igor", "igor@igor.com", "1234j", "89671329812");
        Mockito.when(userRepository.save(userExpected))
                .thenReturn(userExpected);

        User userActual = userService.create(userExpected);

        assertNotNull(userActual, "User must not be null");
        assertEquals("Igor", userActual.getUsername(), "Username must be Igor");
        Mockito.verify(userRepository, times(1)).save(userExpected);
    }

    /**
     **Test Case:** Update an existing user.
     **Expected Result:** User details are successfully updated.
     */
    @Test
    public void updateUserReturnSaveUser() {
        User userExpected = new User("Igor", "igor@igor.com", "1234j", "89671329812");
        userExpected.setId(1L);

        UserCreateDto userActual = new UserCreateDto("Oleg", "oleg@oleg.com", "978523k", "89361234576");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userExpected));

        when(passwordEncoder.encode(userActual.getPassword())).thenReturn(userActual.getPassword());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setPassword(userActual.getPassword());
            return savedUser;
        });

        User userUpdate = userService.updateUser(1L, userActual);

        assertNotNull(userUpdate, "Updated user must not be null");
        assertEquals("Oleg", userUpdate.getUsername(), "Username must be updated to Oleg");
        assertEquals("oleg@oleg.com", userUpdate.getEmail(), "Email must be updated to oleg@oleg.com");
        assertEquals("978523k", userUpdate.getPassword(), "Password must be updated");
        assertEquals("89361234576", userUpdate.getPhoneNumber(), "Phone number must be updated");
    }

    /**
     **Test Case:** Attempt to update a non-existing user.
     **Expected Result:** Throws `UserNotFoundException`.
     */
    @Test
    public void updateUserWhenUserNotFound() {
        userCreateDto = new UserCreateDto("Oleg", "oleg@oleg.com", "password", "89361234576");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1L, userCreateDto);
        });

        assertEquals("User with id 1 not found", userNotFoundException.getMessage());
    }

    /**
     **Test Case:** Delete a user when the user exists.
     **Expected Result:** User is deleted successfully.
     */
    @Test
    public void deleteUserWhenUserExist() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(favoritesService, times(1)).deleteByUser(1L);
        verify(cartService, times(1)).deleteByUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
