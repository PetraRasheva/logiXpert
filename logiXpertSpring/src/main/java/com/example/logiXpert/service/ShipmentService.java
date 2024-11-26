package com.example.logiXpert.service;

import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.model.Shipment;

import java.util.List;

public interface ShipmentService {
    ShipmentDto addShipment(ShipmentDto shipment);

    ShipmentDto updateShipmentById(Integer id, ShipmentDto shipment);

    ShipmentDto getShipmentById(Integer id);

    void deleteShipment(Integer id);

    List<ShipmentDto> getAllShipments();
}
