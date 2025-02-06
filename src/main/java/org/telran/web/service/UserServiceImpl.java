package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.entity.User;
import org.telran.web.exception.UserNotFoundException;
import org.telran.web.repository.UserRepository;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public User updateUser(Long id, UserCreateDto dto) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        existingUser.setUsername(dto.getUsername());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPhoneNumber(dto.getPhoneNumber());
        existingUser.setPassword(dto.getPassword());
        return repository.save(existingUser);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }
}
