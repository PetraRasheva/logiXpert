package com.example.logiXpert.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "couriers")
public class Courier extends User {

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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Set<Shipment> getAssignedShipments() {
        return assignedShipments;
    }

    public void setAssignedShipments(Set<Shipment> assignedShipments) {
        this.assignedShipments = assignedShipments;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }
}
