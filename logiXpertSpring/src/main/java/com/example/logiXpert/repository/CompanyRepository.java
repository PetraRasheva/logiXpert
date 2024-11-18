package com.example.logiXpert.repository;

import com.example.logiXpert.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    void deleteCompanyById(Integer id);
    Optional<Company> findCompanyById(Integer id);
}
