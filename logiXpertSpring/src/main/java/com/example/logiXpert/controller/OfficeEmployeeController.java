package com.example.logiXpert.controller;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.OfficeEmployeeRegistrationDto;
import com.example.logiXpert.service.OfficeEmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class OfficeEmployeeController {

    private final OfficeEmployeeService officeEmployeeService;

    public OfficeEmployeeController(OfficeEmployeeService officeEmployeeService) {
        this.officeEmployeeService = officeEmployeeService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<OfficeEmployeeDto> getOfficeEmployeeById(@PathVariable("id") Integer id) {
        OfficeEmployeeDto officeEmployee = officeEmployeeService.getOfficeEmployeeById(id);
        return new ResponseEntity<>(officeEmployee, HttpStatus.OK);
    }

    @PostMapping("/hire-office-employee")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OfficeEmployeeDto> addOfficeEmployee(@RequestBody OfficeEmployeeRegistrationDto registrationDto) {
        OfficeEmployeeDto newOfficeEmployee = officeEmployeeService.addOfficeEmployee(registrationDto);
        return new ResponseEntity<>(newOfficeEmployee, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<OfficeEmployeeDto> updateOfficeEmployee(@RequestBody OfficeEmployeeDto officeEmployeeDto) {
        OfficeEmployeeDto updatedOfficeEmployee = officeEmployeeService.updateOfficeEmployee(officeEmployeeDto);
        return new ResponseEntity<>(updatedOfficeEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOfficeEmployee(@PathVariable("id") Integer id) {
        officeEmployeeService.deleteOfficeEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}