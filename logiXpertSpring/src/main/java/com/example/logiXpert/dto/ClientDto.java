package com.example.logiXpert.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClientDto(
        int id,
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,
        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "\\d{10,15}", message = "Phone number must be valid and contain 10-15 digits")
        String phone,
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        String email
) {}