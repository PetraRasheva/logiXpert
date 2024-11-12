package com.example.logiXpert.repository;

import com.example.logiXpert.model.Company;
import com.example.logiXpert.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    Optional<Shipment> findShipmentBy(Integer id);

}
