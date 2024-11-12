package com.example.logiXpert.model;

import jakarta.persistence.*;

@Entity
public class OfficeEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;
    private String name;
    private double salary;

    @ManyToOne
    private Office office;

    public OfficeEmployee() {}
    public OfficeEmployee(int id, String name, double salary)
    {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.office = new Office();
    }
}
