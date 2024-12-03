package com.example.logiXpert.service;

import com.example.logiXpert.dto.GetAllShipmentDto;
import com.example.logiXpert.dto.GetShipmentDto;
import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.exception.CompanyNotFoundException;
import com.example.logiXpert.exception.ShipmentNotFoundException;
import com.example.logiXpert.exception.UserNotFoundException;
import com.example.logiXpert.mapper.ClientMapper;
import com.example.logiXpert.mapper.GetShipmentMapper;
import com.example.logiXpert.mapper.ShipmentMapper;
import com.example.logiXpert.model.*;
import com.example.logiXpert.repository.ClientRepository;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.ShipmentRepository;
import com.example.logiXpert.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final GetShipmentMapper getShipmentMapper;
    private final CompanyRepository companyRepository;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final UserRepository userRepository;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper, GetShipmentMapper getShipmentMapper, CompanyRepository companyRepository, ClientRepository clientRepository, ClientMapper clientMapper, UserRepository userRepository) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
        this.getShipmentMapper = getShipmentMapper;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.userRepository = userRepository;
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

        setSender(shipment, shipmentDto);
        setReceiver(shipment, shipmentDto);

        Company company = companyRepository.findById(shipmentDto.companyId())
                .orElseThrow(() -> new CompanyNotFoundException("Company with ID " + shipmentDto.companyId() + " not found"));

        User owner = userRepository.findById(shipmentDto.ownerId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + shipmentDto.ownerId() + " not found"));

        shipment.setOwner(owner);
        shipment.setDeliveryStatus(DeliveryStatus.CREATED);
        calculateTotalRevenue(shipment, company);

        Shipment savedShipment = shipmentRepository.save(shipment);
        return shipmentMapper.toDto(savedShipment);
    }

    @Override
    public GetShipmentDto updateShipment(ShipmentDto shipmentDto) {
        Shipment updateShipment = shipmentMapper.toEntity(shipmentDto);

        setSender(updateShipment, shipmentDto);
        setReceiver(updateShipment, shipmentDto);

        Shipment shipment = shipmentRepository.findShipmentById(shipmentDto.id())
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + shipmentDto.id() + " was not found"));

        User owner = userRepository.findById(shipmentDto.ownerId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + shipmentDto.ownerId() + " not found"));

        shipment.setOwner(owner);
        shipment.setDeliveryStatus(DeliveryStatus.CREATED);

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
    public List<GetAllShipmentDto> getAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAll();
        return shipments.stream().map(shipmentMapper::toGetAllDto).toList();
    }

    public List<Shipment> getShipmentsSentByClient(int clientId) {
        return shipmentRepository.findAllBySenderId(clientId);
    }

    public List<Shipment> getShipmentsReceivedByClient(int clientId) {
        return shipmentRepository.findAllByReceiverId(clientId);
    }


    private void calculateTotalRevenue(Shipment shipment, Company company) {
        shipment.setCompany(company);
        company.setBaseCapital(company.getBaseCapital() + shipment.getPrice());

        companyRepository.save(company);
    }

    private void setSender(Shipment shipment, ShipmentDto shipmentDto) {
        Optional<Client> sender = clientRepository.findByPhone(shipmentDto.sender().phone());
        if (sender.isPresent()) {
            shipment.setSender(sender.get());
        } else {
            clientRepository.save(clientMapper.toEntity(shipmentDto.sender()));
            shipment.setSender(clientMapper.toEntity(shipmentDto.sender()));
        }
    }

    private void setReceiver(Shipment shipment, ShipmentDto shipmentDto) {
        Optional<Client> receiver = clientRepository.findByPhone(shipmentDto.receiver().phone());
        if (receiver.isPresent()) {
            shipment.setReceiver(receiver.get());
        } else {
            clientRepository.save(clientMapper.toEntity(shipmentDto.receiver()));
            shipment.setReceiver(clientMapper.toEntity(shipmentDto.receiver()));
        }
    }

    //TODO: Implement complex requests
}
