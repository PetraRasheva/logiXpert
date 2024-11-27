package com.example.logiXpert.repository;

import com.example.logiXpert.model.Company;
import com.example.logiXpert.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Integer> {
    void deleteOfficeById(Integer id);
    Optional<Office> findOfficeById(Integer id);
    Optional<Office> findByName(String name);

}
