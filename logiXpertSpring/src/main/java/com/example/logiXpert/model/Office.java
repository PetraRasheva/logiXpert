package com.example.logiXpert.model;

public class Office {
    private int id;
    private String address;
    private String name;
    private String phone;

    private int companyId;

    public Office() {}
    public Office(int id, String address, String name, String phone, int companyId) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.companyId = companyId;
    }
}
