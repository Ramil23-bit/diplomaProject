package org.telran.web.converter;

import org.springframework.stereotype.Component;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.User;

/**
 * Converter class for transforming User entities to DTOs and vice versa.
 * Handles the conversion between User, UserCreateDto, and UserResponseDto.
 */
@Component
public class UserCreateConverter implements Converter<User, UserCreateDto, UserResponseDto> {

    /**
     * Converts a User entity to a UserResponseDto.
     *
     * @param user The User entity to convert.
     * @return A UserResponseDto representing the user details.
     */
    @Override
    public UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getRole());
    }

    /**
     * Converts a UserCreateDto to a User entity.
     *
     * @param userCreateDto The DTO containing user creation data.
     * @return The created User entity.
     */
    @Override
    public User toEntity(UserCreateDto userCreateDto) {
        return new User(userCreateDto.getName(), userCreateDto.getEmail(), userCreateDto.getPassword(), userCreateDto.getPhone());
    }
}
