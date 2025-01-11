package org.telran.web.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.User;
import org.telran.web.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

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
        assertEquals(userList, userFromMock);
    }
}
