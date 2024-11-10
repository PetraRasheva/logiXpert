package com.example.logiXpert.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;
    private String name;
    private Long baseCapital;

//    private Set<Office> offices;
//    private Set<Courier> couriers;
//    private Set<OfficeEmployee> officeEmployees;

    public Company() {}
    public Company(String name, Long baseCapital) {
        this.name = name;
        this.baseCapital = baseCapital;
    }


}
