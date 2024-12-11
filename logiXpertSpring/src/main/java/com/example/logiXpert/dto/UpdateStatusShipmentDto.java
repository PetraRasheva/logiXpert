package com.example.logiXpert.dto;

import com.example.logiXpert.model.DeliveryStatus;

import java.time.LocalDateTime;

public record UpdateStatusShipmentDto(Integer id, DeliveryStatus deliveryStatus, LocalDateTime deliveryDate) {
}
