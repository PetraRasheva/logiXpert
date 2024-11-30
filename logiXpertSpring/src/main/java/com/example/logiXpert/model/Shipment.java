package com.example.logiXpert.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Shipment extends BaseEntity {

    private double weight;
    private double price;

    @ManyToOne
    private Company company;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType; // TODO:

    private LocalDateTime shipmentDate;
    private LocalDateTime deliveryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client reciever;

    @ManyToOne(fetch = FetchType.LAZY)
    private Courier courier;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner; //client or employee that registered the shipment

    private String destination;

    public Shipment() {
    }

    public Shipment(double weight, double price,  Client sender, Client recipient, User owner, DeliveryType type, String destination, DeliveryStatus deliveryStatus) {
        this.weight = weight;
        this.price = price;
        this.sender = sender;
        this.reciever = recipient;
        this.owner = owner;
        this.deliveryStatus = deliveryStatus;
        this.shipmentDate = LocalDateTime.now();
        this.courier = new Courier(); // TODO: implement a function to auto-assign couriers
        this.deliveryType = type;
        this.destination = destination; // TODO: Handle differentiating office location VS home address
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

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
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

    public Client getReciever() {
        return reciever;
    }

    public void setReciever(Client reciever) {
        this.reciever = reciever;
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
}
