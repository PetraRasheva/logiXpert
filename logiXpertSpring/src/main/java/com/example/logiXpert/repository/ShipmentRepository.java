package com.example.logiXpert.repository;

import com.example.logiXpert.model.Courier;
import com.example.logiXpert.model.DeliveryStatus;
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

    @Query("SELECT s FROM Shipment s WHERE s.courier IS NULL AND s.deliveryStatus = :deliveryStatus")
    List<Shipment> findAllByCourierIsNullAndDeliveryStatus(@Param("deliveryStatus") DeliveryStatus deliveryStatus);
    List<Shipment> findAllBySenderId(int senderId);
    List<Shipment> findAllByReceiverId(int receiverId);
    List<Shipment> findAllByOwnerId(Integer ownerId);
    List<Shipment> findAllByCourier(Courier courier);
    Optional<Shipment> findShipmentByTrackingNumber(String trackingNum);
}
