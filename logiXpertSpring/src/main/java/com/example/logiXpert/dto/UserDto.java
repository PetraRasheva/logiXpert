package com.example.logiXpert.dto;

import com.example.logiXpert.model.Role;

public record UserDto(int id,
                      String name, String phone, String email, String password, Role role) {
}
