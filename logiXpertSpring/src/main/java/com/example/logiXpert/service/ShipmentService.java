package com.example.logiXpert.service;

import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.model.Shipment;

public interface ShipmentService {
    Shipment addShipment(ShipmentDto shipment);

    Shipment updateShipment(ShipmentDto shipment);

    Shipment getShipmentById(Integer id);

    void deleteShipment(Integer id);
}
