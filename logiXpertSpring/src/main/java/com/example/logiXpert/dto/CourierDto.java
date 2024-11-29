package com.example.logiXpert.dto;

public record CourierDto(
        int id;
        String name;
        Stirng phone;
        String email;
        double salary;
        String officeName;
        String companyName;
        int vehicleId;
){}