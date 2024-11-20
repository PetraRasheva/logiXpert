package com.example.logiXpert.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;
    private String name;
    private double salary;
    private int vehicleId;

    @OneToMany(mappedBy = "courier")
    private Set<Shipment> assignedShipments;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    private Office office;

    public Courier(String name, double salary, int vehicleId)
    {
        this.name = name;
        this.salary = salary;
        this.vehicleId = vehicleId;
        this.office = new Office();
        this.company = new Company();
        this.assignedShipments = new HashSet<>();
    }

    public Courier() {}

    public void assignShipment(Shipment shipment) {
        assignedShipments.add(shipment);
    }

    public void unassignShipment(Shipment shipment) {
        assignedShipments.remove(shipment);
    }
}
