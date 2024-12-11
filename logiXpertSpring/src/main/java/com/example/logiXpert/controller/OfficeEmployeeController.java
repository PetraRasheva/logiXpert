package com.example.logiXpert.controller;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.RegisterOfficeEmployeeDto;
import com.example.logiXpert.service.OfficeEmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@PreAuthorize("hasAuthority('ADMIN')")
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

    @PostMapping("/add")
    public ResponseEntity<OfficeEmployeeDto> addOfficeEmployee(@RequestBody RegisterOfficeEmployeeDto registrationDto) {
        OfficeEmployeeDto newOfficeEmployee = officeEmployeeService.addOfficeEmployee(registrationDto);
        return new ResponseEntity<>(newOfficeEmployee, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('OFFICE_EMPLOYEE')")
    public ResponseEntity<OfficeEmployeeDto> updateOfficeEmployee(@Valid @RequestBody OfficeEmployeeDto officeEmployeeDto) {
        OfficeEmployeeDto updatedOfficeEmployee = officeEmployeeService.updateOfficeEmployee(officeEmployeeDto);
        return new ResponseEntity<>(updatedOfficeEmployee, HttpStatus.OK);
    }

    @PutMapping("/admin/update")
    public ResponseEntity<OfficeEmployeeDto> adminUpdateOfficeEmployee(@Valid @RequestBody OfficeEmployeeDto officeEmployeeDto) {
        OfficeEmployeeDto updatedEmployee = officeEmployeeService.updateOfficeEmployeeByAdmin(officeEmployeeDto);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteOfficeEmployee(@PathVariable("id") Integer id) {
        officeEmployeeService.deleteOfficeEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}