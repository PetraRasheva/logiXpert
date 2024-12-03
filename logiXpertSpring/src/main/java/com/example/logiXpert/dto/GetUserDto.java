package com.example.logiXpert.dto;

import com.example.logiXpert.model.ERole;

import java.util.Set;

public record GetUserDto(
        String name,
        String phone,
        String email,
        Set<ERole> roles
) {
}