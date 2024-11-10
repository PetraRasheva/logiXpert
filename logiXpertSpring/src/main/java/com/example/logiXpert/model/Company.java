package com.example.logiXpert.model;

import java.util.Set;

public class Company {
    private int id;
    private String name;
    private Long baseCapital;

    private Set<Office> offices;
    private Set<Courier> couriers;
    private Set<OfficeEmployee> officeEmployees;

    public Company() {}
    public Company(int id, String name, Long baseCapital) {
        this.id = id;
        this.name = name;
        this.baseCapital = baseCapital;
    }
}
