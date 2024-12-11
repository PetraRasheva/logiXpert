package com.example.logiXpert.dto;

public record CourierDto(
        Integer id,
        String name,
        String phone,
        String email,
        double salary,
        String officeName,
        String companyName,
        int vehicleId
){}