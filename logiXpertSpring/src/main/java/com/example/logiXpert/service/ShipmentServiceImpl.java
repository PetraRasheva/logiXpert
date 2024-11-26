package com.example.logiXpert.service;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.exception.ShipmentNotFoundException;
import com.example.logiXpert.mapper.ShipmentMapper;
import com.example.logiXpert.model.Shipment;
import com.example.logiXpert.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
    }

    @Override
    public ShipmentDto addShipment(ShipmentDto shipmentDto) {
        Shipment shipment = shipmentRepository.save(shipmentMapper.toEntity(shipmentDto));
        return shipmentMapper.toDto(shipment);
    }

    @Override
    public ShipmentDto updateShipmentById(Integer id, ShipmentDto shipmentDto) {
        Shipment updateShipment = shipmentMapper.toEntity(shipmentDto);
        Shipment shipment = shipmentRepository.findShipmentById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " was not found"));
        shipment.setShipmentDate(updateShipment.getShipmentDate());
        shipment.setDeliveryDate(updateShipment.getDeliveryDate());
        shipment.setDestination(updateShipment.getDestination());
        shipment.setPrice(updateShipment.getPrice());
        shipment.setDeliveryType(updateShipment.getDeliveryType());
        shipment.setWeight(updateShipment.getWeight());
        shipment.setDeliveryStatus(updateShipment.getDeliveryStatus());
        // Hope there is a better approach
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return shipmentMapper.toDto(updatedShipment);
    }

    @Override
    public ShipmentDto getShipmentById(Integer id) {
        Shipment shipment = shipmentRepository.findShipmentById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " was not found"));
        return shipmentMapper.toDto(shipment);
    }

    @Override
    public void deleteShipment(Integer id) {
        shipmentRepository.deleteShipmentById(id);
    }

    //TODO: Implement complex requests
}
