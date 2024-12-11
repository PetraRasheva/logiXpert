package com.example.logiXpert.dto;

import com.example.logiXpert.model.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ShipmentDto(
        Integer id,
        double weight,
        double price,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime shipmentDate,
        String source,
        String destination,
        Integer companyId,
        ClientDto sender,
        ClientDto receiver,
        Integer ownerId,
        double profit,
        DeliveryStatus deliveryStatus
) {}
