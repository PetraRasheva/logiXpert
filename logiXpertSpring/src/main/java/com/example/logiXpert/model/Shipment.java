package com.example.logiXpert.model;

import java.time.LocalDate;

public class Shipment {
    private int id;
    private double weight;
    private double price;
    private DeliveryStatus status;
    private DeliveryType type;
    private String deliveryAddress;
    private LocalDate shipmentDate;
    private LocalDate deliveryDate;

    private int senderId;
    private int recipientId;
    private int courierId;
    private int officeEmployeeId;
    private int officeShipmentId; // Office that processed the shipment
    private int officeDestinationId;

    public Shipment() {
    }

    //Construct obj with DeliveryType.TO_OFFICE, note the last param
    public Shipment(double weight, double price, int senderId, int recipientId, int officeShipmentId, int officeEmployeeId, int officeDestinationId) {
        this.weight = weight;
        this.price = price;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.officeShipmentId = officeShipmentId;
        this.officeEmployeeId = officeEmployeeId;
        this.status = DeliveryStatus.CREATED;
        this.shipmentDate = LocalDate.now();
        this.courierId = 0; // TODO: implement a function to auto-assign couriers
        this.officeDestinationId = officeDestinationId;
    }

    //Construct obj with DeliveryType.TO_ADDRESS
    public Shipment(double weight, double price, int senderId, int recipientId, int officeShipmentId, int officeEmployeeId, String deliveryAddress) {
        this.weight = weight;
        this.price = price;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.officeShipmentId = officeShipmentId;
        this.officeEmployeeId = officeEmployeeId;
        this.status = DeliveryStatus.CREATED;
        this.shipmentDate = LocalDate.now();
        this.courierId = 0; // TODO: implement a function to auto-assign couriers
        this.deliveryAddress = deliveryAddress;
    }
}
