package com.example.logiXpert.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;
    private String address;
    private String name;
    private String phone;

    @ManyToOne
    private Company company;

//    The Office class does not need to maintain a list of employees and couriers.
//    Instead, rely on querying the Employee class for employees associated with a specific office when needed.

    public Office() {
    }

    public Office(String address, String name, String phone) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.company = new Company();
    }


}
