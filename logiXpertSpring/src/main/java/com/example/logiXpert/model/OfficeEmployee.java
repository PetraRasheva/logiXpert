package com.example.logiXpert.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
public class OfficeEmployee extends User {

    private double salary;

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

    public OfficeEmployee() {
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Office office;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    public OfficeEmployee(String name, String phone, String email, String password, double salary, Office office, Company company) {
        super(name, phone, email, password);
        this.salary = salary;
        this.office = office;
        this.company = company;
    }

    // base entity for the id inherited by the user

    // TODO: Assign employee to company/ office
}
