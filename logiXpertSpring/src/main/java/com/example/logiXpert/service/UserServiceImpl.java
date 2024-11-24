package com.example.logiXpert.service;

import com.example.logiXpert.dto.CredentialsDto;
import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.exception.UserException;
import com.example.logiXpert.mapper.UserMapper;
import com.example.logiXpert.model.User;
import com.example.logiXpert.repository.UserRepository;
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
        userRepository.deleteUserById(id);
    }

    @Override
    public UserDto login(CredentialsDto credentialsDto) {
       User user = userRepository.findUserByEmail(credentialsDto.email()).orElseThrow(() -> new UserException("UserDto " + credentialsDto.email() + " was not found", HttpStatus.NOT_FOUND));
       if (credentialsDto.password().equals(user.getPassword())) { // TODO: implement password encoder
           return userMapper.toDto(user);
       }
       throw new UserException("UserDto " + credentialsDto.email() + " has invalid password", HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findUserByEmail(signUpDto.email());

        if(user.isPresent()){
            throw new UserException("UserDto " + signUpDto.email() + " has already been registered", HttpStatus.BAD_REQUEST);
        }

        User newUser = userMapper.signUpToUser(signUpDto);
        //newUser.setPassword(passwordEncoder.encode(signUpDto.password())); // TODO: use encode
        newUser.setPassword(signUpDto.password());
        User savedUser = userRepository.save(newUser);

        return null;
    }

    //TODO: Implement complex requests
}
