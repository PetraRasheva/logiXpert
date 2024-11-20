package com.example.logiXpert.service;

import com.example.logiXpert.dto.CredentialsDto;
import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.exception.UserException;
import com.example.logiXpert.mapper.UserMapper;
import com.example.logiXpert.model.Role;
import com.example.logiXpert.model.User;
import com.example.logiXpert.repository.AdminRepository;
import com.example.logiXpert.repository.ClientRepository;
import com.example.logiXpert.repository.OfficeEmployeeRepository;
import com.example.logiXpert.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final OfficeEmployeeRepository officeEmployeeRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, ClientRepository clientRepository, OfficeEmployeeRepository officeEmployeeRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.officeEmployeeRepository = officeEmployeeRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public User addUser(User user) {
        if(user.getRole() == Role.ADMIN){
           // adminRepository.save(user);
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findUserById(id).orElseThrow(() -> new UserException("User with id " + id + " was not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteUserById(id);
    }

    @Override
    public UserDto login(CredentialsDto credentialsDto) {
       User user = userRepository.findUserByEmail(credentialsDto.email()).orElseThrow(() -> new UserException("User " + credentialsDto.email() + " was not found", HttpStatus.NOT_FOUND));
       if (passwordEncoder.matches(credentialsDto.password(), user.getPassword())) {
           return userMapper.toUserDto(user);
       }
       throw new UserException("User " + credentialsDto.email() + " has invalid password", HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findUserByEmail(signUpDto.email());

        if(user.isPresent()){
            throw new UserException("User " + signUpDto.email() + " has already been registered", HttpStatus.BAD_REQUEST);
        }

        User newUser = userMapper.signUpToUser(signUpDto);
        newUser.setPassword(passwordEncoder.encode(signUpDto.password()));
        User savedUser = userRepository.save(newUser);

        return null;
    }

    //TODO: Implement complex requests
}
