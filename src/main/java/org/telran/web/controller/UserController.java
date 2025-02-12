package org.telran.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Provides endpoints for user authentication, retrieval, and management.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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
     * @param request The sign-in request containing credentials.
     * @return The authentication response with the JWT token.
     */
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody SignInRequest request) {
        logger.info("User login attempt: {}", request.email());
        return authenticationService.authenticate(request);
    }

    /**
     * Retrieves the current user's role.
     *
     * @return A map containing the user's role.
     */
    @Operation(summary = "Get current user role", description = "Retrieves the role of the currently authenticated user.")
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Map<String, String> getCurrentUserRole() {
        logger.info("Fetching current user's role");
        String role = userService.getCurrentUserRole();
        logger.info("Current user's role: {}", role);
        return Map.of("role", role);
    }

    /**
     * Retrieves all users (Admin only).
     *
     * @return List of user response DTOs.
     */
    @Operation(summary = "Get all users", description = "Retrieves a list of all users (Admin only).")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAll() {
        logger.info("Fetching all users");
        List<UserResponseDto> users = userService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        logger.info("Total users retrieved: {}", users.size());
        return users;
    }

    /**
     * Retrieves a user by their ID (Admin only).
     *
     * @param id The ID of the user.
     * @return The user response DTO.
     */
    @Operation(summary = "Get user by ID", description = "Retrieves details of a specific user by their ID (Admin only).")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto get(@PathVariable("id") Long id) {
        logger.info("Fetching user with ID: {}", id);
        UserResponseDto user = converter.toDto(userService.getById(id));
        logger.info("User retrieved: {}", user);
        return user;
    }

    /**
     * Registers a new user.
     *
     * @param dto The DTO containing user details.
     * @return The created user response DTO.
     */
    @Operation(summary = "Register a new user", description = "Registers a new user account.")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {
        logger.info("Received request to register user: {}", dto.getEmail());
        User user = converter.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.create(user);
        UserResponseDto responseDto = converter.toDto(savedUser);
        logger.info("User registered successfully with ID: {}", responseDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
