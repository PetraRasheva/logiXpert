package com.example.logiXpert.dto;

public record RegisterOfficeEmployeeDto(
        int id,
        String name,
        String phone,
        String email,
        double salary,
        String officeName,
        String password
) {}