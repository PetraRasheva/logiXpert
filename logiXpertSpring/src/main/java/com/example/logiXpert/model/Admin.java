package com.example.logiXpert.model;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class Admin extends User {

    public Admin(String name, String phone, String email, String password, Role role) {
        super(name, phone, email, password, Role.ADMIN);
    }
}
