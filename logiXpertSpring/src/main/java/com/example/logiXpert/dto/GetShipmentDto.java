package com.example.logiXpert.dto;

import com.example.logiXpert.model.DeliveryStatus;
import com.example.logiXpert.model.DeliveryType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

// We use it to retrieve full shipment data from the database - update / find by id
public record GetShipmentDto(int id, double weight, double price, DeliveryStatus deliveryStatus, DeliveryType deliveryType,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime shipmentDate, @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime deliveryDate,
                             String destination, Integer senderId, Integer receiverId, Integer courierId, Integer ownerId) {
}