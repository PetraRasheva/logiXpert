package com.example.logiXpert.dto;

import com.example.logiXpert.constraint.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpDto(@NotBlank(message = "Name is required")
                        String name,
                        @NotBlank(message = "Phone is required")
                        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
                        String phone,
                        @Email(message = "Email must be valid")
                        @NotBlank(message = "Email is required")
                        String email,
                        @ValidPassword
                        String password) {
}
