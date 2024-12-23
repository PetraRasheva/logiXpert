package com.example.logiXpert.dto;

public record CourierDto(
        Integer id,
        String name,
        String phone,
        String email,
        Double salary,
        String officeName,
        String companyName,
        Integer vehicleId
){}