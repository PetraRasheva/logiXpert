package com.example.logiXpert.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Shipment extends BaseEntity {

    private double weight;
    private double price;
    private double profit;

    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

   @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private LocalDateTime shipmentDate;
    private LocalDateTime deliveryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Courier courier;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner; //client or employee that registered the shipment

    private String source;
    private String destination;
    private double addressFee;


    public Shipment() {
    }

    public Shipment(double weight, double price,  Client sender, Client recipient, User owner, String source, String destination, DeliveryStatus deliveryStatus, double addressFee) {
        this.weight = weight;
        this.price = price;
        this.sender = sender;
        this.receiver = recipient;
        this.owner = owner;
        this.deliveryStatus = deliveryStatus;
        this.shipmentDate = LocalDateTime.now();
        this.source = source;
        this.destination = destination;
        this.addressFee = addressFee;
    }

    @PrePersist
    private void generateTrackingNumber() {
        this.trackingNumber = "SHP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LocalDateTime getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(LocalDateTime shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public Client getReceiver() {
        return receiver;
    }

    public void setReceiver(Client receiver) {
        this.receiver = receiver;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getAddressFee() {
        return addressFee;
    }

    public void setAddressFee(double addressFee) {
        this.addressFee = addressFee;
    }
}
