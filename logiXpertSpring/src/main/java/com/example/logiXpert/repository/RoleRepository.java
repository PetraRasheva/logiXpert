package com.example.logiXpert.repository;

import com.example.logiXpert.model.Role;
import com.example.logiXpert.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}