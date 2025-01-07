package com.example.logiXpert.dto;

import com.example.logiXpert.model.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

// We use it to retrieve full shipment data from the database - update / find by id
public record GetShipmentDto(
        Integer id,
        String trackingNumber,
        double weight,
        double price,
        DeliveryStatus deliveryStatus,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime shipmentDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime deliveryDate,
        String source,
        String destination,
        ClientDto sender,
        ClientDto receiver,
        OwnerDto owner,
        String courierName
) {}
