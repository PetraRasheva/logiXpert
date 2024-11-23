package com.example.logiXpert.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class OfficeEmployee extends User{

    private String name;
    private double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    private Office office;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    public OfficeEmployee(String name, String phone, String email, String password, double salary) {
        //super(name, phone, email, password, Role.EMPLOYEE);
        this.salary = salary;
    }

    // base entity for the id inherited by the user

    // TODO: Assign employee to company/ office
}
