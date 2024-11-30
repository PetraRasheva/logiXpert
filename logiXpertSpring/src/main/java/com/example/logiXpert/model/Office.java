package com.example.logiXpert.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
public class Office extends BaseEntity {

    private String address;
    private String name;
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

//    The Office class does not need to maintain a list of employees and couriers.
//    Instead, rely on querying the Employee class for employees associated with a specific office when needed.

    public Office(String address, String name, String phone) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.company = new Company();
    }

    public Office() {}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
