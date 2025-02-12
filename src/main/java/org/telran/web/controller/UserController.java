package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.User;
import org.telran.web.security.AuthenticationService;
import org.telran.web.security.model.JwtAuthenticationResponse;
import org.telran.web.security.model.SignInRequest;
import org.telran.web.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for managing users.
 * Provides endpoints for authentication, user retrieval, creation, updating, and deletion.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Converter<User, UserCreateDto, UserResponseDto> converter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request SignInRequest containing user credentials.
     * @return JwtAuthenticationResponse containing the authentication token.
     */
    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody SignInRequest request){
        return authenticationService.authenticate(request);
    }

    /**
     * Retrieves the role of the currently authenticated user.
     *
     * @return Map containing the user's role.
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Map<String, String> getCurrentUserRole(){
        String role = userService.getCurrentUserRole();
        return Map.of("role", role);
    }

    /**
     * Retrieves all users. Only accessible by admins.
     *
     * @return List of UserResponseDto representing all users.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAll() {
        return userService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by their ID. Only accessible by admins.
     *
     * @param id ID of the user.
     * @return UserResponseDto representing the found user.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto get(@PathVariable("id") Long id) {
        return converter.toDto(userService.getById(id));
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return UserResponseDto representing the current user.
     */
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserResponseDto get() {
        return converter.toDto(userService.getById(userService.getCurrentUserId()));
    }

    /**
     * Registers a new user.
     *
     * @param dto UserCreateDto containing the user details.
     * @return ResponseEntity with the created user details.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserCreateDto dto) {
        User user = converter.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userService.create(user);
        UserResponseDto responseDto = converter.toDto(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Updates a user by their ID. Only accessible by admins.
     *
     * @param id ID of the user to update.
     * @param dto UserCreateDto containing updated user details.
     * @return ResponseEntity with the updated UserResponseDto.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody @Valid UserCreateDto dto) {
        User updatedUser = userService.updateUser(id, dto);
        UserResponseDto responseDto = converter.toDto(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Updates the currently authenticated user.
     *
     * @param dto UserCreateDto containing updated user details.
     * @return ResponseEntity with the updated UserResponseDto.
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserResponseDto> update(@RequestBody @Valid UserCreateDto dto) {
        User updatedUser = userService.updateUser(userService.getCurrentUserId(), dto);
        UserResponseDto responseDto = converter.toDto(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Updates the role of a user by their ID. Only accessible by admins.
     *
     * @param id ID of the user whose role will be updated.
     */
    @PutMapping("/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateRole(@PathVariable(name = "id") Long id){
        userService.updateUserRole(id);
    }

    /**
     * Deletes a user by their ID. Only accessible by admins.
     *
     * @param id ID of the user to delete.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes the currently authenticated user.
     *
     * @return ResponseEntity with no content.
     */
    @DeleteMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUserById(userService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}
