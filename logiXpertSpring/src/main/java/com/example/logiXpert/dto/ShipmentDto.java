package com.example.logiXpert.dto;


import com.example.logiXpert.model.DeliveryStatus;
import com.example.logiXpert.model.DeliveryType;

import java.time.LocalDateTime;

public record ShipmentDto(int id,double weight,double price, DeliveryStatus deliveryStatus, DeliveryType deliveryType,
                          LocalDateTime shipmentDate, String destination) {
}
