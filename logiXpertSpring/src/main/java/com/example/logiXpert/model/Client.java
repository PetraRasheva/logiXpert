package com.example.logiXpert.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class Client extends User {
    public Client(String name, String phone, String email, String password) {
        super(name, phone, email, password, Role.CLIENT);
    }
}
