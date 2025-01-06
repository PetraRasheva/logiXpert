package com.example.logiXpert.controller;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.dto.GetUserDto;
import com.example.logiXpert.service.CompanyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable("id") Integer id) {
        CompanyDto company = companyService.getCompanyById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CompanyDto> addCompany(@RequestBody CompanyDto company) {
        CompanyDto newCompany = companyService.addCompany(company);
        return new ResponseEntity<>(newCompany, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<CompanyDto> updateCompany(@RequestBody CompanyDto company) {
        CompanyDto updateCompany = companyService.updateCompany(company);
        return new ResponseEntity<>(updateCompany, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable("id") Integer id) {
        companyService.deleteCompany(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<GetUserDto>> getAllEmployees() {
        // get couriers + office employees
        List<GetUserDto> employees = companyService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}/revenue-by-date-range")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Double> getRevenue(
            @PathVariable("id") Integer id,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        double revenue = companyService.calculateRevenueDateRange(id, start, end);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/{id}/total-revenue")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Double> getTotalRevenue(
            @PathVariable("id") Integer id) {
        double revenue = companyService.calculateRevenue(id);
        return ResponseEntity.ok(revenue);
    }
}