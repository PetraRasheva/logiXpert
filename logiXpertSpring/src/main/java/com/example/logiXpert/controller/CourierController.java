package com.example.logiXpert.controller;

import com.example.logiXpert.dto.CourierDto;
import com.example.logiXpert.service.CourierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courier")
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

//    @PostMapping("/add")
//    public ResponseEntity<CourierDto> addCourier(@RequestBody CourierDto courierDto) {
//        CourierDto newCourier = courierService.addCourier(courierDto);
//        return new ResponseEntity<>(newCourier, HttpStatus.CREATED);
//    }

    @PutMapping("/update")
    public ResponseEntity<CourierDto> updateCourier(@RequestBody CourierDto courierDto) {
        CourierDto updatedCourier = courierService.updateCourier(courierDto);
        return new ResponseEntity<>(updatedCourier, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourier(@PathVariable("id") Integer id) {
        courierService.deleteCourier(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
