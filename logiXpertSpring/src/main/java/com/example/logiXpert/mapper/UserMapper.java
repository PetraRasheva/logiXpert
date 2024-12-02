package com.example.logiXpert.mapper;


import com.example.logiXpert.dto.GetUserDto;
import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    GetUserDto toGetUserDto(User user);
    User signUpToUser(SignUpDto signUpDto);
    User toEntity(UserDto userDto);
}
