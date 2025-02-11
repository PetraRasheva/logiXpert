package com.example.logiXpert.service;

import com.example.logiXpert.dto.*;
import com.example.logiXpert.exception.*;
import com.example.logiXpert.mapper.ClientMapper;
import com.example.logiXpert.mapper.GetShipmentMapper;
import com.example.logiXpert.mapper.ShipmentMapper;
import com.example.logiXpert.model.*;
import com.example.logiXpert.repository.*;
import com.itextpdf.barcodes.exceptions.WriterException;
import com.itextpdf.io.exceptions.IOException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final OfficeEmployeeRepository officeEmployeeRepository;
    private final PdfGeneratorServiceImpl pdfGeneratorServiceImpl;


    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper, GetShipmentMapper getShipmentMapper, CompanyRepository companyRepository, ClientRepository clientRepository, ClientMapper clientMapper, UserRepository userRepository, CourierRepository courierRepository, PdfGeneratorServiceImpl pdfGeneratorServiceImpl, OfficeEmployeeRepository officeEmployeeRepository) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
        this.getShipmentMapper = getShipmentMapper;
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.userRepository = userRepository;
        this.courierRepository = courierRepository;
        this.pdfGeneratorServiceImpl = pdfGeneratorServiceImpl;
        this.officeEmployeeRepository= officeEmployeeRepository;
    }

    @Override
    public List<GetAllShipmentDto> getShipmentsCreatedByEmployee(Integer employeeId) {
        OfficeEmployee employee = officeEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new OfficeEmployeeNotFoundException("Office employee with id " + employeeId + " not found"));

        List<Shipment> shipments = shipmentRepository.findAllByOwnerId(employee.getId());
        return shipments.stream().map(shipmentMapper::toGetAllDto).toList();
    }

    @Override
    public List<GetAllShipmentDto> getShipmentsCreatedByClient(Integer clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + clientId + " not found"));

        List<Shipment> shipments = shipmentRepository.findAllByOwnerId(client.getId());
        return shipments.stream().map(shipmentMapper::toGetAllDto).toList();
    }

    @Override
    public List<GetAllShipmentDto> getShipmentsByCourierId(Integer courierId) {
        Courier courier = courierRepository.findCourierById(courierId)
                .orElseThrow(() -> new CourierNotFoundException("Courier with ID " + courierId + " not found"));

        List<Shipment> shipments = shipmentRepository.findAllByCourier(courier);
        return shipments.stream()
                .map(shipmentMapper::toGetAllDto)
                .toList();
    }

    @Override
    public byte[] getShipmentInvoice(Integer shipmentId) {
        try {
            Shipment shipment = shipmentRepository.findShipmentById(shipmentId)
                    .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with ID: " + shipmentId));

            GetShipmentDto shipmentDto = getShipmentMapper.toDto(shipment);
            return pdfGeneratorServiceImpl.generateShipmentInvoice(shipmentDto);
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generating shipment invoice: " + e.getMessage(), e);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (com.google.zxing.WriterException e) {
            throw new RuntimeException(e);
        }
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

        shipmentMapper.updateShipmentFromDto(shipmentDto, shipment);

        if (shipmentDto.ownerId() != null) {
            User owner = userRepository.findById(shipmentDto.ownerId())
                    .orElseThrow(() -> new UserNotFoundException("User with ID " + shipmentDto.ownerId() + " not found"));
            shipment.setOwner(owner);
        }

        setSender(shipment, shipmentDto);
        setReceiver(shipment, shipmentDto);

        Shipment updatedShipment = shipmentRepository.save(shipment);
        return getShipmentMapper.toDto(updatedShipment);
    }

    @Override
    public GetShipmentDto getShipmentById(Integer id) {
        Shipment shipment = shipmentRepository.findShipmentById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " was not found"));
        return getShipmentMapper.toDto(shipment);
    }

    @Transactional
    public void deleteShipment(Integer id) {
        shipmentRepository.deleteShipmentById(id);
    }

    @Override
    public void deleteShipmentByTrackingNumber(String trackingNumber) {
        Shipment shipment = shipmentRepository.findShipmentByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with tracking number " + trackingNumber + " was not found"));

        shipmentRepository.delete(shipment);
    }

    @Override
    public List<GetAllShipmentDto> getAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAll();
        return shipments.stream().map(shipmentMapper::toGetAllDto).toList();
    }

    public List<Shipment> getShipmentsSentByClient(int clientId) {
        return shipmentRepository.findNonDeliveredShipmentsByOwnerId(clientId);
    }

    public List<Shipment> getShipmentsReceivedByClient(int clientId) {
        return shipmentRepository.findDeliveredShipmentsByOwnerId(clientId);
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
        shipment.setDeliveryStatus(DeliveryStatus.TRANSIT);
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return getShipmentMapper.toDto(updatedShipment);
    }

    @Override
    public GetShipmentDto unassignShipmentFromCourier(Integer shipmentId) {
        Shipment shipment = shipmentRepository.findShipmentById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with ID: " + shipmentId));

        if (shipment.getCourier() == null) {
            throw new IllegalStateException("Shipment is not assigned to any courier");
        }

        shipment.setCourier(null);
        Shipment updatedShipment = shipmentRepository.save(shipment);

        return getShipmentMapper.toDto(updatedShipment);
}

    @Override
    public GetShipmentDto assignShipmentToCourierByTrackingNumber(String trackingNumber, Integer courierId) {
        Shipment shipment = shipmentRepository.findShipmentByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with tracking number: " + trackingNumber));

        Courier courier = courierRepository.findCourierById(courierId)
                .orElseThrow(() -> new CourierNotFoundException("Courier not found with ID: " + courierId));

        shipment.setCourier(courier);
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return getShipmentMapper.toDto(updatedShipment);
    }

    @Override
    public GetShipmentDto unassignShipmentFromCourierByTrackingNumber(String trackingNumber) {
        Shipment shipment = shipmentRepository.findShipmentByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with tracking number: " + trackingNumber));

        if (shipment.getCourier() == null) {
            throw new IllegalStateException("Shipment is not assigned to any courier");
        }

        shipment.setCourier(null);
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

    @Override
    public List<GetAllShipmentDto> getUnassignedShipmentsForCourier(Integer courierId) {
        courierRepository.findById(courierId)
                .orElseThrow(() -> new CourierNotFoundException("Courier with ID " + courierId + " not found"));

        List<Shipment> unassignedShipments = shipmentRepository.findAllByCourierIsNullAndDeliveryStatus(DeliveryStatus.CREATED);

        return unassignedShipments.stream()
                .map(shipmentMapper::toGetAllDto)
                .collect(Collectors.toList());
    }

    private void calculateTotalRevenue(Shipment shipment, Company company) {
        shipment.setCompany(company);
        company.setBaseCapital(company.getBaseCapital() + shipment.getProfit());

        companyRepository.save(company);
    }

    private void setSender(Shipment shipment, ShipmentDto shipmentDto) {
        Optional<Client> existingSender = clientRepository.findByPhone(shipmentDto.sender().phone());
        if (existingSender.isPresent()) {
            shipment.setSender(existingSender.get());
        } else {
            Client newSender = clientMapper.toEntity(shipmentDto.sender());
            Client savedSender = clientRepository.save(newSender);
            shipment.setSender(savedSender);
        }
    }

    private void setReceiver(Shipment shipment, ShipmentDto shipmentDto) {
        Optional<Client> existingReceiver = clientRepository.findByPhone(shipmentDto.receiver().phone());
        if (existingReceiver.isPresent()) {
            shipment.setReceiver(existingReceiver.get());
        } else {
            Client newReceiver = clientMapper.toEntity(shipmentDto.receiver());
            Client savedReceiver = clientRepository.save(newReceiver);
            shipment.setReceiver(savedReceiver);
        }
    }
}
