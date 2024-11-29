package com.example.logiXpert.service;

import com.example.logiXpert.dto.GetShipmentDto;
import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.model.Shipment;

import java.time.LocalDateTime;
import java.util.List;

public interface ShipmentService {
    ShipmentDto addShipment(ShipmentDto shipment);

    GetShipmentDto updateShipment(ShipmentDto shipment);

    GetShipmentDto getShipmentById(Integer id);

    void deleteShipment(Integer id);

    List<ShipmentDto> getAllShipments();

    double calculateTotalRevenue(Integer companyId, LocalDateTime startDate, LocalDateTime endDate);
}
