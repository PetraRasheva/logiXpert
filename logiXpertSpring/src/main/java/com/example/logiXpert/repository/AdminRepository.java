package com.example.logiXpert.repository;

import com.example.logiXpert.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    void deleteAdminById(Integer id);
    Optional<Admin> findAdminById(Integer id);
}
