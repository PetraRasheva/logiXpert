package com.example.logiXpert.mapper;


import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User signUpToUser(SignUpDto signUpDto);
    User toEntity(UserDto userDto);
}
