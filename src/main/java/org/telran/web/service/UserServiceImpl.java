package org.telran.web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.entity.Cart;
import org.telran.web.entity.User;
import org.telran.web.enums.Role;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.exception.UserAlreadyExistsException;
import org.telran.web.exception.UserNotFoundException;
import org.telran.web.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserService.
 * Handles business logic for user management, including authentication, updates, and deletion.
 */
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

    /**
     * Retrieves all users from the repository.
     *
     * @return List of User entities representing all users.
     */
    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id ID of the user.
     * @return The found User entity.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    /**
     * Creates a new user and assigns a cart to them.
     *
     * @param user User entity containing user details.
     * @return The created User entity.
     * @throws UserAlreadyExistsException if the email is already in use.
     * @throws BadArgumentsException if form data is incorrect.
     */
    @Transactional
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

    /**
     * Updates an existing user's details.
     *
     * @param userId ID of the user to update.
     * @param dto DTO containing updated user details.
     * @return The updated User entity.
     * @throws UserNotFoundException if the user is not found.
     */
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

    /**
     * Updates the role of a user to admin.
     *
     * @param id ID of the user whose role will be updated.
     */
    @Override
    public void updateUserRole(Long id) {
        User currentUser = getById(getCurrentUserId());
        if(currentUser.getRole().equals(Role.ROLE_ADMIN)){
            User updateRoleUser = getById(id);
            updateRoleUser.setRole(Role.ROLE_ADMIN);
            repository.save(updateRoleUser);
        }
    }

    /**
     * Deletes a user by their ID along with their associated favorites and cart.
     *
     * @param id ID of the user to delete.
     * @throws UserNotFoundException if the user is not found.
     */
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

    /**
     * Retrieves a user by their email.
     *
     * @param email Email of the user.
     * @return The found User entity.
     * @throws UserNotFoundException if the user is not found.
     */
    @Override
    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    /**
     * Retrieves the role of the currently authenticated user.
     *
     * @return The role of the current user.
     */
    @Override
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getAuthorities().toString() : null;
    }

    /**
     * Retrieves the ID of the currently authenticated user.
     *
     * @return The ID of the current user.
     * @throws UserNotFoundException if the authenticated user is not found.
     */
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

    /**
     * Retrieves the email of the currently authenticated user.
     *
     * @return The email of the current user.
     */
    @Override
    public String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : null;
    }
}
