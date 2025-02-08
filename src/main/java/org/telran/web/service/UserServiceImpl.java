package org.telran.web.service;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.entity.User;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.exception.UserAlreadyExistsException;
import org.telran.web.exception.UserNotFoundException;
import org.telran.web.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private CartService cartService;

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
        if(repository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        try {
            return repository.save(user);
        } catch (Exception e) {
            throw new BadArgumentsException("Form is not completed correctly");
        }
    }

    @Override
    public User updateUser(Long id, UserCreateDto dto) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        existingUser.setUsername(dto.getName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPhoneNumber(dto.getPhone());
        existingUser.setPassword(dto.getPassword());
        return repository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        favoritesService.deleteByUser(id);
        cartService.deleteByUser(id);
        repository.deleteById(id);
    }

    @Override
    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }
}
