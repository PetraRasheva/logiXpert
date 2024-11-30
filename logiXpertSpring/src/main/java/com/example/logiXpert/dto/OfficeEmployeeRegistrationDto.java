package com.example.logiXpert.dto;

public record OfficeEmployeeRegistrationDto(
        String name,
        String phone,
        String email,
        double salary,
        String officeName,
        String companyName,
        String role,
        String password
) {}