package com.example.logiXpert.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client extends User {
    public Client() {
    }

    public Client(String name, String phone, String email, String password) {
        super(name, phone, email, password);
    }
}
