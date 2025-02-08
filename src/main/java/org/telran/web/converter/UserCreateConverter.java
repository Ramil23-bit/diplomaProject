package org.telran.web.converter;

import org.springframework.stereotype.Component;
import org.telran.web.dto.UserCreateDto;
import org.telran.web.dto.UserResponseDto;
import org.telran.web.entity.User;

@Component
public class UserCreateConverter implements Converter<User, UserCreateDto, UserResponseDto>{

    @Override
    public UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber());
    }

    @Override
    public User toEntity(UserCreateDto userCreateDto) {
        return new User(userCreateDto.getName(), userCreateDto.getEmail(), userCreateDto.getPassword(), userCreateDto.getPhone());
    }
}
