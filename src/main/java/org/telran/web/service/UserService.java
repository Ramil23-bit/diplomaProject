package org.telran.web.service;

import org.telran.web.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();
    User getById(Long id);
    User create(User user);
}
