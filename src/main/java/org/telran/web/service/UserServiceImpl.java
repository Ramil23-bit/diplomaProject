package org.telran.web.service;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.entity.Cart;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            User savedUser = repository.save(user);
            cartService.createCart(new Cart(savedUser));
            return savedUser;
        } catch (Exception e) {
            throw new BadArgumentsException("Form is not completed correctly");
        }
    }

    @Override
    public User updateUser(Long userId, UserCreateDto dto) {
        User existingUser = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        existingUser.setUsername(dto.getName());
        existingUser.setPhoneNumber(dto.getPhone());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        try {
            return repository.save(existingUser);
        } catch (Exception e) {
            throw new BadArgumentsException("Form is not completed correctly");
        }
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
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().toString();
        }
        return null;
    }

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String name = authentication.getName();
            return repository.findByEmail(name)
                    .map(User::getId)
                    .orElseThrow(() -> new UserNotFoundException("User with email " + name + " not found"));
        }
        throw new UserNotFoundException("No authenticated user found");
    }


    @Override
    public String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }
}
