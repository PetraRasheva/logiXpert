package com.example.logiXpert.dto;

import com.example.logiXpert.model.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record GetAllShipmentDto(String trackingNumber, double price, DeliveryStatus deliveryStatus,
                                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime shipmentDate) {
}
