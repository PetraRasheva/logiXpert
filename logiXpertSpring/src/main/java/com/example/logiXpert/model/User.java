package com.example.logiXpert.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private String email;
    private String password;

    @OneToMany(mappedBy = "owner")
    private Set<Shipment> processedShipments;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private void registerShipment() {
        //TODO: We need to construct the Shipment depending of the Shipment type
    }
}
