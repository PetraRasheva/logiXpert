package com.example.logiXpert.dto;


import com.example.logiXpert.model.Client;
import com.example.logiXpert.model.DeliveryStatus;
import com.example.logiXpert.model.DeliveryType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ShipmentDto(int id, double weight, double price, DeliveryType deliveryType,
                          @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime shipmentDate, String destination, int companyId,
                          Client sender, Client receiver, int userId) {
}
