package com.example.logiXpert.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    public Admin() {

    }

    public Admin(String name, String phone, String email, String password, Role role) {
        //super(name, phone, email, password, Role.ADMIN);
    }
}
