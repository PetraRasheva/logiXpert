package com.example.logiXpert.controller;

import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.model.Shipment;
import com.example.logiXpert.service.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    //TODO: Refactor controllers to use Dto models

    @GetMapping("/find/{id}")
    public ResponseEntity<Shipment> getShipmentById (@PathVariable("id") Integer id) {
        Shipment shipment = shipmentService.getShipmentById(id);
        return new ResponseEntity<>(shipment, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Shipment> addShipment(@RequestBody ShipmentDto shipmentDto) {
        Shipment newShipment = shipmentService.addShipment(shipmentDto);
        return new ResponseEntity<>(newShipment, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Shipment> updateShipment(@RequestBody ShipmentDto shipmentDto) {
        Shipment updateShipment = shipmentService.updateShipment(shipmentDto);
        return new ResponseEntity<>(updateShipment, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteShipment(@PathVariable("id") Integer id) {
        shipmentService.deleteShipment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
