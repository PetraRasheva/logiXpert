package com.example.logiXpert.service;

import com.example.logiXpert.dto.GetAllShipmentDto;
import com.example.logiXpert.dto.GetShipmentDto;
import com.example.logiXpert.dto.ShipmentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ShipmentService {
    ShipmentDto addShipment(ShipmentDto shipment);

    GetShipmentDto updateShipment(ShipmentDto shipment);

    GetShipmentDto getShipmentById(Integer id);

    void deleteShipment(Integer id);

    List<GetAllShipmentDto> getAllShipments();

    double calculateTotalRevenueForPeriod(Integer companyId, LocalDateTime startDate, LocalDateTime endDate);
}
