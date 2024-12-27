package com.example.logiXpert.service;

import com.example.logiXpert.dto.GetAllShipmentDto;
import com.example.logiXpert.dto.GetShipmentDto;
import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.dto.UpdateStatusShipmentDto;
import com.example.logiXpert.model.Shipment;
import com.itextpdf.barcodes.exceptions.WriterException;
import com.itextpdf.io.exceptions.IOException;

import java.time.LocalDateTime;
import java.util.List;

public interface ShipmentService {
    ShipmentDto addShipment(ShipmentDto shipment);

    GetShipmentDto updateShipment(ShipmentDto shipment);

    GetShipmentDto getShipmentById(Integer id);

    void deleteShipment(Integer id);

    List<GetAllShipmentDto> getAllShipments();

    double calculateTotalRevenueForPeriod(Integer companyId, LocalDateTime startDate, LocalDateTime endDate);

    List<GetAllShipmentDto> getNotDeliveredShipments();
    
    List<Shipment> getShipmentsSentByClient(int clientId);

    List<Shipment> getShipmentsReceivedByClient(int clientId);

    GetShipmentDto getShipmentByTrackingNumber(String trackingNum);

    GetShipmentDto updateShipmentStatus(UpdateStatusShipmentDto uShipment);

    GetShipmentDto assignShipmentToCourier(Integer courierId, Integer shipmentId);

    List<GetAllShipmentDto> getShipmentsCreatedByEmployee(Integer employeeId);

    List<GetAllShipmentDto> getShipmentsCreatedByClient(Integer clientId);

    byte[] getShipmentInvoice(Integer shipmentId);
}

