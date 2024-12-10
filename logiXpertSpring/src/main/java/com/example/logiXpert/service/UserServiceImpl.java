package com.example.logiXpert.service;

import com.example.logiXpert.dto.CredentialsDto;
import com.example.logiXpert.dto.GetUserDto;
import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.exception.UserException;
import com.example.logiXpert.mapper.UserMapper;
import com.example.logiXpert.model.Client;
import com.example.logiXpert.model.ERole;
import com.example.logiXpert.model.Role;
import com.example.logiXpert.model.User;
import com.example.logiXpert.repository.ClientRepository;
import com.example.logiXpert.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.logiXpert.repository.RoleRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserMapper userMapper, PasswordEncoder passwordEncoder, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
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
    public GetUserDto getUserById(Integer id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UserException("UserDto with id " + id + " was not found", HttpStatus.NOT_FOUND));
        return userMapper.toGetUserDto(user);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserException("User with id " + id + " was not found", HttpStatus.NOT_FOUND);
        }
        userRepository.deleteUserById(id);
    }

    public boolean hasRole(User user, ERole role) {
        return user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(role));
    }

    @Override
    public UserDto login(@Valid CredentialsDto credentialsDto) {
        User user = userRepository.findUserByEmail(credentialsDto.email())
                .orElseThrow(() -> new UserException("User with email " + credentialsDto.email() + " was not found", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(credentialsDto.password(), user.getPassword())) {
            throw new UserException("Invalid password for email " + credentialsDto.email(), HttpStatus.BAD_REQUEST);
        }

        return userMapper.toDto(user);
    }

    @Override
    public UserDto signUp(SignUpDto signUpDto) {
        if (userRepository.existsUserByEmail(signUpDto.email())) {
            throw new UserException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        Role clientRole = roleRepository.findByName(ERole.CLIENT)
                .orElseThrow(() -> new RuntimeException("Error: Role CLIENT is not found."));

        Client newClient = new Client(
                signUpDto.name(),
                signUpDto.phone(),
                signUpDto.email(),
                passwordEncoder.encode(signUpDto.password())
        );

        newClient.getRoles().add(clientRole);

        Client savedClient = clientRepository.save(newClient);
        return userMapper.toDto(savedClient);
    }

    @Override
    public List<GetUserDto> getAllWithRoles(Set<ERole> roles) {
        List<User> employees = userRepository.findAllByRoles(roles);
        List<GetUserDto> employeeDtos = employees.stream()
                .map(user -> new GetUserDto(
                        user.getId(),
                        user.getName(),
                        user.getPhone(),
                        user.getEmail(),
                        user.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet())
                )).toList();
        return employeeDtos;
    }
}