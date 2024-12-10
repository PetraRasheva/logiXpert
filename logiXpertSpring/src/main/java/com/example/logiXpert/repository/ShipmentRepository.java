package com.example.logiXpert.repository;

import com.example.logiXpert.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    void deleteShipmentById(Integer id);
    Optional<Shipment> findShipmentById(Integer id);

    @Query("SELECT s FROM Shipment s WHERE s.company.id = :companyId AND s.shipmentDate BETWEEN :startDate AND :endDate")
    List<Shipment> findShipmentsByCompanyAndDateRange(
            @Param("companyId") Integer companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM Shipment s WHERE s.deliveryStatus = 'CREATED' OR s.deliveryStatus = 'TRANSIT'")
    List<Shipment> findShipmentsNotDelivered();
    List<Shipment> findAllBySenderId(int senderId);
    List<Shipment> findAllByReceiverId(int receiverId);
}
