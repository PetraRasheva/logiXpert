package com.example.logiXpert.repository;

import com.example.logiXpert.model.Company;
import com.example.logiXpert.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierRepository extends JpaRepository<Courier, Integer> {
    Optional<Courier> findCourierById(Integer id);

}
