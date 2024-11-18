package com.example.logiXpert.repository;

import com.example.logiXpert.model.User;
import com.example.logiXpert.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    void deleteUserById(Integer id);
    Optional<User> findUserById(Integer id);
}
