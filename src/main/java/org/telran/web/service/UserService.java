package org.telran.web.service;

import jakarta.validation.Valid;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();
    User getById(Long id);
    User create(User user);
    User getByEmail(String email);

    User updateUser(Long userId, UserCreateDto dto);

    void updateUserRole(Long id);

    void deleteUserById(Long id);

    String getCurrentEmail();
    Long getCurrentUserId();
    String getCurrentUserRole();
}
