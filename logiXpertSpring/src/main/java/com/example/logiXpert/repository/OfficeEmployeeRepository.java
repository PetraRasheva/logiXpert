package com.example.logiXpert.repository;

import com.example.logiXpert.model.OfficeEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeEmployeeRepository extends JpaRepository<OfficeEmployee, Integer> {
    void deleteOfficeEmployeeById(Integer id);
    Optional<OfficeEmployee> findOfficeEmployeeById(Integer id);
}
