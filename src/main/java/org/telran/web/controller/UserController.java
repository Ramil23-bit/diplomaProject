package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.web.converter.Converter;
import org.telran.web.dto.CategoryResponseDto;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.User;
import org.telran.web.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/shop_users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private Converter<User, UserCreateDto, UserResponseDto> converter;

    @GetMapping
    public List<UserResponseDto> getAll() {
        return service.getAll().stream()
                .map(user -> converter.toDto(user))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDto get(@PathVariable("id") Long id) {
        return converter.toDto(service.getById(id));
    }

    @PostMapping
    public UserResponseDto create(@RequestBody UserCreateDto dto) {
        return converter.toDto(service.create(converter.toEntity(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody @Valid UserCreateDto dto) {
        User updatedUser = service.updateUser(id, dto);
        UserResponseDto responseDto = converter.toDto(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(name = "id") Long id){
        service.deleteUserById(id);
    }
}
