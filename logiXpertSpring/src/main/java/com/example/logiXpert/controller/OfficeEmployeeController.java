package com.example.logiXpert.controller;

import com.example.logiXpert.model.OfficeEmployee;
import com.example.logiXpert.service.OfficeEmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class OfficeEmployeeController {
    private final OfficeEmployeeService officeEmployeeService;

    public OfficeEmployeeController(OfficeEmployeeService officeEmployeeService) {
        this.officeEmployeeService = officeEmployeeService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<OfficeEmployee> getOfficeEmployeeById(@PathVariable("id") Integer id) {
        OfficeEmployee officeEmployee = officeEmployeeService.getOfficeEmployeeById(id);
        return new ResponseEntity<>(officeEmployee, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<OfficeEmployee> addOfficeEmployee(@RequestBody OfficeEmployee officeEmployee) {
        OfficeEmployee newOfficeEmployee = officeEmployeeService.addOfficeEmployee(officeEmployee);
        return new ResponseEntity<>(newOfficeEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<OfficeEmployee> updateOfficeEmployee(@RequestBody OfficeEmployee officeEmployee) {
        OfficeEmployee updateOfficeEmployee = officeEmployeeService.updateOfficeEmployee(officeEmployee);
        return new ResponseEntity<>(updateOfficeEmployee, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOfficeEmployee(@PathVariable("id") Integer id) {
        officeEmployeeService.deleteOfficeEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
