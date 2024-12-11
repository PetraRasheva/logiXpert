package com.example.logiXpert.service;

import com.example.logiXpert.dto.*;
import com.example.logiXpert.exception.CompanyNotFoundException;
import com.example.logiXpert.exception.CourierNotFoundException;
import com.example.logiXpert.exception.ShipmentNotFoundException;
import com.example.logiXpert.exception.UserNotFoundException;
import com.example.logiXpert.mapper.ClientMapper;
import com.example.logiXpert.mapper.GetShipmentMapper;
import com.example.logiXpert.mapper.ShipmentMapper;
import com.example.logiXpert.model.*;
import com.example.logiXpert.repository.*;
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
    private final CourierRepository courierRepository;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper, GetShipmentMapper getShipmentMapper, CompanyRepository companyRepository, ClientRepository clientRepository, ClientMapper clientMapper, UserRepository userRepository, CourierRepository courierRepository) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
        this.getShipmentMapper = getShipmentMapper;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.userRepository = userRepository;
        this.courierRepository = courierRepository;
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
        Shipment shipment = shipmentRepository.findShipmentById(shipmentDto.id())
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + shipmentDto.id() + " was not found"));

        if (shipmentDto.ownerId() != null) {
            User owner = userRepository.findById(shipmentDto.ownerId())
                    .orElseThrow(() -> new UserNotFoundException("User with ID " + shipmentDto.ownerId() + " not found"));
            shipment.setOwner(owner);
        }

        setSender(shipment, shipmentDto);
        setReceiver(shipment, shipmentDto);

        shipment.setProfit(shipmentDto.profit());

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

    @Override
    public GetShipmentDto getShipmentByTrackingNumber(String trackingNum) {
        Shipment shipment = shipmentRepository.findShipmentByTrackingNumber(trackingNum)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with tracking number " + trackingNum + " was not found"));
        return getShipmentMapper.toDto(shipment);
    }

    @Override
    public GetShipmentDto updateShipmentStatus(UpdateStatusShipmentDto uShipment) {
        Shipment shipment = shipmentRepository.findShipmentById(uShipment.id())
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + uShipment.id() + " was not found"));

        shipment.setDeliveryStatus(uShipment.deliveryStatus());
        if(uShipment.deliveryStatus() == DeliveryStatus.DELIVERED) {
            shipment.setDeliveryDate(LocalDateTime.now());
        }
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return getShipmentMapper.toDto(updatedShipment);
    }

    @Override
    public GetShipmentDto assignShipmentToCourier(Integer courierId, Integer shipmentId) {
        Shipment shipment = shipmentRepository.findShipmentById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + shipmentId + " was not found"));

        // Check if shipment is already assigned
        if (shipment.getCourier() != null) {
            throw new IllegalStateException("Shipment is already assigned to a courier");
        }

        Courier courier = courierRepository.findCourierById(courierId)
                        .orElseThrow(() -> new CourierNotFoundException(("Courier with id " + courierId + " was not found")));

        shipment.setCourier(courier);
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return getShipmentMapper.toDto(updatedShipment);
    }

    @Override
    public List<GetAllShipmentDto> getNotDeliveredShipments() {
        List<Shipment> shipments = shipmentRepository.findShipmentsNotDelivered();
        return shipments.stream()
                .map(shipmentMapper::toGetAllDto)
                .toList();
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
}
