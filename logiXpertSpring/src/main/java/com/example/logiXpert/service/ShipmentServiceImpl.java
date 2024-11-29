package com.example.logiXpert.service;

import com.example.logiXpert.dto.GetShipmentDto;
import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.exception.ShipmentNotFoundException;
import com.example.logiXpert.mapper.GetShipmentMapper;
import com.example.logiXpert.mapper.ShipmentMapper;
import com.example.logiXpert.model.Company;
import com.example.logiXpert.model.DeliveryStatus;
import com.example.logiXpert.model.Shipment;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final GetShipmentMapper getShipmentMapper;
    private final CompanyRepository companyRepository;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper, GetShipmentMapper getShipmentMapper, CompanyRepository companyRepository) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
        this.getShipmentMapper = getShipmentMapper;
        this.companyRepository = companyRepository;
    }

    @Override
    public double calculateTotalRevenueForPeriod(Integer companyId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Shipment> shipments = shipmentRepository.findShipmentsByCompanyAndDateRange(companyId, startDate, endDate);
        return shipments.stream()
                .mapToDouble(Shipment::getPrice)
                .sum();
    }

    @Override
    public ShipmentDto addShipment(ShipmentDto shipmentDto) {
        Shipment shipment = shipmentMapper.toEntity(shipmentDto);

        // SET STATUS
        shipment.setDeliveryStatus(DeliveryStatus.CREATED);

        calculateTotalRevenue(shipment, shipmentDto);

        Shipment savedShipment = shipmentRepository.save(shipment);
        return shipmentMapper.toDto(savedShipment);
    }

    private void calculateTotalRevenue(Shipment shipment, ShipmentDto shipmentDto) {
        Company company = companyRepository.findById(shipmentDto.companyId())
                .orElseThrow(() -> new IllegalArgumentException("Company with ID " + shipmentDto.companyId() + " not found"));

        shipment.setCompany(company);
        company.setBaseCapital(company.getBaseCapital() + shipment.getPrice());

        companyRepository.save(company);
    }

    @Override
    public GetShipmentDto updateShipment(ShipmentDto shipmentDto) {
        Shipment updateShipment = shipmentMapper.toEntity(shipmentDto);
        Shipment shipment = shipmentRepository.findShipmentById(shipmentDto.id())
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + shipmentDto.id() + " was not found"));
        shipment.setShipmentDate(updateShipment.getShipmentDate());
        shipment.setDeliveryDate(updateShipment.getDeliveryDate());
        shipment.setDestination(updateShipment.getDestination());
        shipment.setPrice(updateShipment.getPrice());
        shipment.setDeliveryType(updateShipment.getDeliveryType());
        shipment.setWeight(updateShipment.getWeight());
        shipment.setDeliveryStatus(updateShipment.getDeliveryStatus());
        // Hope there is a better approach
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return getShipmentMapper.toDto(updatedShipment);
    }

    @Override
    public GetShipmentDto getShipmentById(Integer id) {
        Shipment shipment = shipmentRepository.findShipmentById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " was not found"));
        return getShipmentMapper.toDto(shipment);
    }

    @Override
    public void deleteShipment(Integer id) {
        shipmentRepository.deleteShipmentById(id);
    }

    @Override
    public List<ShipmentDto> getAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAll();
        return shipments.stream().map(shipmentMapper::toDto).toList();
    }


    //TODO: Implement complex requests
}
