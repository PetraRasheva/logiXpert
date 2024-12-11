package com.example.logiXpert.controller;

import com.example.logiXpert.dto.CourierDto;
import com.example.logiXpert.dto.RegisterCourierDto;
import com.example.logiXpert.service.CourierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courier")
@PreAuthorize("hasAuthority('ADMIN')")
public class CourierController {

    private final CourierService courierService;

    CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CourierDto> getCourierById(@PathVariable("id") Integer id) {
        CourierDto courier = courierService.getCourierById(id);
        return new ResponseEntity<>(courier, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CourierDto> addCourier(@RequestBody RegisterCourierDto registrationDto) {
        CourierDto newCourier = courierService.addCourier(registrationDto);
        return new ResponseEntity<>(newCourier, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('COURIER')")
    public ResponseEntity<CourierDto> updateCourier(@RequestBody CourierDto courierDto) {
        CourierDto updatedCourier = courierService.updateCourier(courierDto);
        return new ResponseEntity<>(updatedCourier, HttpStatus.OK);
    }

    // Endpoint for admins to update courier-specific properties
    @PutMapping("/update-admin")
    public ResponseEntity<CourierDto> updateOfficeEmployeeByAdmin(@RequestBody CourierDto courierDto) {
        CourierDto updatedCourier = courierService.updateCourierByAdmin(courierDto);
        return new ResponseEntity<>(updatedCourier, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourier(@PathVariable("id") Integer id) {
        courierService.deleteCourier(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
