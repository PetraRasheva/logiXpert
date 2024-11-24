package com.example.logiXpert.controller;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update/{id}")
    public ResponseEntity<CompanyDto> updateCompanyById(@PathVariable("id") Integer id, @RequestBody CompanyDto company) {
        CompanyDto updateCompany = companyService.updateCompanyById(id, company);
        return new ResponseEntity<>(updateCompany, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable("id") Integer id) {
        companyService.deleteCompany(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}