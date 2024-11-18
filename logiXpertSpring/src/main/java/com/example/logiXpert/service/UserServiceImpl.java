package com.example.logiXpert.service;

import com.example.logiXpert.exception.UserNotFoundException;
import com.example.logiXpert.model.User;
import com.example.logiXpert.repository.UserRepository;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found"));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteUserById(id);
    }

    //TODO: Implement complex requests
}
