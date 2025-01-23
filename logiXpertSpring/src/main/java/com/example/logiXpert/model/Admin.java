package com.example.logiXpert.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
public class Admin extends User {

    public Admin() {

    }

    public Admin(String name, String phone, String email, String password) {
        super(name, phone, email, password);
    }
}
