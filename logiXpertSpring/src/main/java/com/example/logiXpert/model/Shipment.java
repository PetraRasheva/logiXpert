package com.example.logiXpert.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;
    private double weight;
    private double price;

    @ManyToOne
    private Company company;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType; // TODO:

    private LocalDate shipmentDate;
    private LocalDate deliveryDate;

    @ManyToOne
    private Client sender;

    @ManyToOne
    private Client recipient;

    @ManyToOne
    private Courier courier;

    @ManyToOne
    private User owner; //client or employee that registered the shipment

    private String destination;

    public Shipment() {
    }

    public Shipment(double weight, double price,  Client sender, Client recipient, User owner, DeliveryType type, String destination) {
        this.weight = weight;
        this.price = price;
        this.sender = sender;
        this.recipient = recipient;
        this.owner = owner;
        this.status = DeliveryStatus.CREATED;
        this.shipmentDate = LocalDate.now();
        this.courier = new Courier(); // TODO: implement a function to auto-assign couriers
        this.deliveryType = type;
        this.destination = destination; // TODO: Handle differentiating office location VS home address
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
