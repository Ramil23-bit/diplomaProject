package org.telran.web.service;

import jakarta.validation.Valid;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.entity.User;

import java.util.List;

/**
 * Service interface for managing users.
 * Provides methods for user retrieval, creation, update, and deletion.
 */
public interface UserService {

    /**
     * Retrieves all users.
     *
     * @return List of User entities representing all users.
     */
    List<User> getAll();

    /**
     * Retrieves a user by their ID.
     *
     * @param id ID of the user.
     * @return The found User entity.
     */
    User getById(Long id);

    /**
     * Creates a new user.
     *
     * @param user User entity containing user details.
     * @return The created User entity.
     */
    User create(User user);

    /**
     * Retrieves a user by their email.
     *
     * @param email Email of the user.
     * @return The found User entity.
     */
    User getByEmail(String email);

    /**
     * Updates an existing user's details.
     *
     * @param userId ID of the user to update.
     * @param dto    DTO containing updated user details.
     * @return The updated User entity.
     */
    User updateUser(Long userId, @Valid UserCreateDto dto);

    /**
     * Updates the role of a user by their ID.
     *
     * @param id ID of the user whose role will be updated.
     */
    void updateUserRole(Long id);

    /**
     * Deletes a user by their ID.
     *
     * @param id ID of the user to delete.
     */
    void deleteUserById(Long id);

    /**
     * Retrieves the email of the currently authenticated user.
     *
     * @return The email of the current user.
     */
    String getCurrentEmail();

    /**
     * Retrieves the ID of the currently authenticated user.
     *
     * @return The ID of the current user.
     */
    Long getCurrentUserId();

    /**
     * Retrieves the role of the currently authenticated user.
     *
     * @return The role of the current user.
     */
    String getCurrentUserRole();
}
