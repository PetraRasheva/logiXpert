package com.example.logiXpert.dto;

public record OfficeEmployeeDto(
        int id,
        String name,
        String phone,
        String email,
        double salary,
        String officeName,
        String companyName
) {}