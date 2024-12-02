package com.example.logiXpert.service;

import com.example.logiXpert.dto.CredentialsDto;
import com.example.logiXpert.dto.GetUserDto;
import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    UserDto addUser(UserDto user);

    UserDto updateUser(UserDto user);

    GetUserDto getUserById(Integer id);

    void deleteUser(Integer id);

    UserDto login(CredentialsDto credentials);

    UserDto signUp(SignUpDto signUpDto);

    //TODO:
    //регистрирай/създай пратка
    //сметни цена на пратка
    //проследи пратка
    //виж пратки (user.type == client -> getShipmentsByClientID | user.type == employee -> getAll)
    //справки (офис служителя да има отделен таб, който само той да вижда, съдържащ листнатите справки)
}
