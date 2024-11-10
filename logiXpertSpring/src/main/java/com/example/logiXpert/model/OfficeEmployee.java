package com.example.logiXpert.model;

public class OfficeEmployee {
    private int id;
    private String name;
    private double salary;

    private int officeId;

    public OfficeEmployee() {}
    public OfficeEmployee(int id, String name, double salary, int officeId)
    {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.officeId = officeId;
    }
}
