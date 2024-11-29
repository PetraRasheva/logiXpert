package com.example.logiXpert.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
public class Company extends BaseEntity {

    private String name;
    private double baseCapital;

    @OneToMany(mappedBy = "company")
    private Set<Office> offices;

    @OneToMany(mappedBy = "company")
    private Set<Courier> couriers;

    @OneToMany(mappedBy = "company")
    private Set<OfficeEmployee> officeEmployees;

    @OneToMany(mappedBy = "company")
    private Set<Shipment> shipments;

    public Company() {}

    public Company(String name, double baseCapital) {
        this.name = name;
        this.baseCapital = baseCapital;
        this.offices = new HashSet<>();
        this.couriers = new HashSet<>();
        this.officeEmployees = new HashSet<>();
        this.shipments = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBaseCapital() {
        return baseCapital;
    }

    public void setBaseCapital(double baseCapital) {
        this.baseCapital = baseCapital;
    }

    public Set<Office> getOffices() {
        return offices;
    }

    public void setOffices(Set<Office> offices) {
        this.offices = offices;
    }

    public Set<Courier> getCouriers() {
        return couriers;
    }

    public void setCouriers(Set<Courier> couriers) {
        this.couriers = couriers;
    }

    public Set<OfficeEmployee> getOfficeEmployees() {
        return officeEmployees;
    }

    public void setOfficeEmployees(Set<OfficeEmployee> officeEmployees) {
        this.officeEmployees = officeEmployees;
    }

    public Set<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(Set<Shipment> shipments) {
        this.shipments = shipments;
    }

    @Override
    public String toString() {
        return "Company{" +
                ", name='" + name + '\'' +
                ", baseCapital=" + baseCapital +
                '}';
    }
}
