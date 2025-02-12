package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * Provides endpoints for authentication, user registration, and user management.
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
     * Authenticates a user and generates a JWT token.
     *
     * @param request The authentication request containing email and password.
     * @return JWT authentication response.
     */
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    })
    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody SignInRequest request) {
        return authenticationService.authenticate(request);
    }

    /**
     * Retrieves the current logged-in user's role.
     *
     * @return A map containing the user's role.
     */
    @Operation(summary = "Get current user's role", description = "Retrieves the role of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Map<String, String> getCurrentUserRole() {
        String role = userService.getCurrentUserRole();
        return Map.of("role", role);
    }

    /**
     * Retrieves all registered users (Admin only).
     *
     * @return List of user response DTOs.
     */
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users. Accessible by ADMIN users only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAll() {
        return userService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Registers a new user.
     *
     * @param dto The DTO containing user details.
     * @return The created user response DTO.
     */
    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserCreateDto dto) {
        User user = converter.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.create(user);
        UserResponseDto responseDto = converter.toDto(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * Deletes the currently authenticated user.
     */
    @Operation(summary = "Delete current user", description = "Removes the currently authenticated user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUserById(userService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}