package com.example.logiXpert.service;

import com.example.logiXpert.model.Shipment;

public interface ShipmentService {
    Shipment addShipment(Shipment shipment);

    Shipment updateShipment(Shipment shipment);

    Shipment getShipmentById(Integer id);

    void deleteShipment(Integer id);
}
