package com.example.logiXpert.model;

public class Courier {
    private int id;
    private String name;
    private double salary;
    private int vehicleId;

    private int officeId;

    public Courier() {}
    public Courier(int id, String name, double salary, int vehicleId, int officeId)
    {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.vehicleId = vehicleId;
        this.officeId = officeId;
    }

}
