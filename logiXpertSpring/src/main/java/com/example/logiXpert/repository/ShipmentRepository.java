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

    @Query("SELECT s FROM Shipment s WHERE s.deliveryStatus = 'CREATED' OR s.deliveryStatus = 'TRANSIT'")
    List<Shipment> findShipmentsNotDelivered();

    @Query("SELECT s FROM Shipment s WHERE s.courier IS NULL AND s.deliveryStatus = :deliveryStatus")
    List<Shipment> findAllByCourierIsNullAndDeliveryStatus(@Param("deliveryStatus") DeliveryStatus deliveryStatus);

    @Query("SELECT s FROM Shipment s WHERE s.owner.id = :ownerId AND s.deliveryStatus = 'DELIVERED'")
    List<Shipment> findDeliveredShipmentsByOwnerId(@Param("ownerId") Integer ownerId);

    @Query("SELECT s FROM Shipment s WHERE s.owner.id = :ownerId AND s.deliveryStatus != 'DELIVERED'")
    List<Shipment> findNonDeliveredShipmentsByOwnerId(@Param("ownerId") Integer ownerId);

    List<Shipment> findAllByOwnerId(Integer ownerId);
    List<Shipment> findAllByCourier(Courier courier);
    Optional<Shipment> findShipmentByTrackingNumber(String trackingNum);

    @Query("SELECT SUM(s.profit) FROM Shipment s WHERE s.company.id = :companyId")
    Double calculateRevenueByCompanyId(@Param("companyId") Integer companyId);

    @Query("SELECT SUM(s.price) FROM Shipment s WHERE s.company.id = :companyId AND s.shipmentDate BETWEEN :startDate AND :endDate")
    Double calculateRevenueByCompanyAndDateRange(
            @Param("companyId") Integer companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
