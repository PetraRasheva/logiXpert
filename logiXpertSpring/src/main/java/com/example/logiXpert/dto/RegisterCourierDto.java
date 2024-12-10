package com.example.logiXpert.dto;

public record RegisterCourierDto(
        int id,
        String name,
        String phone,
        String email,
        double salary,
        String officeName,
        String password,
        int vehicleId
) {}
