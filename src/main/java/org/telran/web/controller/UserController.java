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

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody SignInRequest request){
        return authenticationService.authenticate(request);
    }

    @GetMapping("/me")
    public Map<String, String> getCurrentUserRole(){
        String role = userService.getCurrentUserRole();
        return Map.of("role", role);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAll() {
        return userService.getAll().stream()
                .map(user -> converter.toDto(user))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto get(@PathVariable("id") Long id) {
        return converter.toDto(userService.getById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserCreateDto dto) {
        User user = converter.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userService.create(user);
        UserResponseDto responseDto = converter.toDto(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody @Valid UserCreateDto dto) {
        User updatedUser = userService.updateUser(id, dto);
        UserResponseDto responseDto = converter.toDto(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/role/{id}")
    public void updateRole(@PathVariable(name = "id") Long id){
        userService.updateUserRole(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
