package com.example.logiXpert.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class OfficeEmployee extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;
    private String name;
    private double salary;

    @ManyToOne
    private Office office;

    @ManyToOne
    private Company company;

    public OfficeEmployee() {
        super();
    }

    public OfficeEmployee(String email, String password, String name, double salary)
    {
        super(email, password);
        this.name = name;
        this.salary = salary;
        this.office = new Office();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Set<Shipment> getProcessedShipments() {
        return processedShipments;
    }

    public void setProcessedShipments(Set<Shipment> processedShipments) {
        this.processedShipments = processedShipments;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
