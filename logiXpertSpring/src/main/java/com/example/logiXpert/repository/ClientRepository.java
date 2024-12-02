package com.example.logiXpert.repository;

import com.example.logiXpert.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    void deleteClientById(Integer id);
    Optional<Client> findClientById(Integer id);
    Optional<Client> findByPhone(String phone);
}
