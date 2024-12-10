package com.example.logiXpert.repository;

import com.example.logiXpert.model.ERole;
import com.example.logiXpert.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {
    void deleteUserById(Integer id);
    Optional<User> findUserById(Integer id);
    Optional<User> findUserByEmail(String email);
    Boolean existsUserByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name IN :roles")
    List<User> findAllByRoles(Set<ERole> roles);
}
