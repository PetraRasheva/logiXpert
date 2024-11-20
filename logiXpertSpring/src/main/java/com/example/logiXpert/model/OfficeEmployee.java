package com.example.logiXpert.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class OfficeEmployee extends User{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false, updatable = false)
//    private Integer id;
    private String name;
    private double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    private Office office;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    public OfficeEmployee(String name, String phone, String email, String password, double salary) {
        super(name, phone, email, password, Role.EMPLOYEE);
        this.name = name;
        this.salary = salary;
        this.office = new Office();
        this.company = new Company();
    }
}
