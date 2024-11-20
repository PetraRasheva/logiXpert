package com.example.logiXpert.controller;

import com.example.logiXpert.model.Courier;
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
    public ResponseEntity<Courier> getCourierById(@PathVariable("id") Integer id) {
        Courier courier = courierService.getCourierById(id);
        return new ResponseEntity<>(courier, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Courier> addCourier(@RequestBody Courier courier) {
        Courier newCourier = courierService.addCourier(courier);
        return new ResponseEntity<>(newCourier, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Courier> updateCourier(@RequestBody Courier courier) {
        Courier updateCourier = courierService.updateCourier(courier);
        return new ResponseEntity<>(updateCourier, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourier(@PathVariable("id") Integer id) {
        courierService.deleteCourier(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
