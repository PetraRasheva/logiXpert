package com.example.logiXpert.service;

import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.exception.ShipmentNotFoundException;
import com.example.logiXpert.model.Shipment;
import com.example.logiXpert.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }
    
    @Override
    public Shipment addShipment(ShipmentDto shipment) {
        return shipmentRepository.save(shipment);
    }

    @Override
    public Shipment updateShipment(ShipmentDto shipment) {
        return shipmentRepository.save(shipment);
    }

    @Override
    public Shipment getShipmentById(Integer id) {
        return shipmentRepository.findShipmentById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " was not found"));
    }

    @Override
    public void deleteShipment(Integer id) {
        shipmentRepository.deleteShipmentById(id);
    }

    //TODO: Implement complex requests
}
