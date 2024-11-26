package com.example.logiXpert.service;

import com.example.logiXpert.dto.CredentialsDto;
import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.exception.UserException;
import com.example.logiXpert.mapper.UserMapper;
import com.example.logiXpert.model.User;
import com.example.logiXpert.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

// TODO: Enable spring security and use encrypted password

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        //TODO: Is it a good approach ? TRIED IT AND DOESN t WORK, .User cannot be cast to.Admin
//        if(user.getRole() == Role.ADMIN){
//           adminRepository.save(user);
//        }
        User user = userRepository.save(userMapper.toEntity(userDto));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        if (!userRepository.existsById(userDto.id())) {
            throw new UserException("User with id " + userDto.id() + " was not found", HttpStatus.NOT_FOUND);
        }
        User user = userRepository.save(userMapper.toEntity(userDto));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepository.findUserById(id).orElseThrow(() -> new UserException("UserDto with id " + id + " was not found", HttpStatus.NOT_FOUND));
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserException("User with id " + id + " was not found", HttpStatus.NOT_FOUND);
        }
        userRepository.deleteUserById(id);
    }

    @Override
    public UserDto login(@Valid CredentialsDto credentialsDto) {
        User user = userRepository.findUserByEmail(credentialsDto.email())
                .orElseThrow(() -> new UserException("User with email " + credentialsDto.email() + " was not found", HttpStatus.NOT_FOUND));

        // TODO: Replace with password encoder
        if (!credentialsDto.password().equals(user.getPassword())) {
            throw new UserException("Invalid password for email " + credentialsDto.email(), HttpStatus.BAD_REQUEST);
        }
        return userMapper.toDto(user);
    }

    @Override
    public UserDto signUp(@Valid SignUpDto signUpDto) {
        // Проверка за вече съществуващ потребител
        if (userRepository.findUserByEmail(signUpDto.email()).isPresent()) {
            throw new UserException("User with email " + signUpDto.email() + " has already been registered", HttpStatus.BAD_REQUEST);
        }

        // Създаване на нов потребител от DTO-то
        User newUser = userMapper.signUpToUser(signUpDto);

        // Хеширане на паролата
        // TODO: Uncomment this after adding PasswordEncoder bean
        // newUser.setPassword(passwordEncoder.encode(signUpDto.password()));

        // Засега оставяме паролата в обикновен текст (неправилно в продукционна среда)
        newUser.setPassword(signUpDto.password());

        // Запис в базата данни
        User savedUser = userRepository.save(newUser);

        // Връщане на UserDto
        return userMapper.toDto(savedUser);
    }

    //TODO: Implement complex requests
}
