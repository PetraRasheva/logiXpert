package com.example.logiXpert.mapper;


import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.model.User;

public interface UserMapper {
    UserDto toUserDto(User user);

    User signUpToUser(SignUpDto signUpDto);
}
