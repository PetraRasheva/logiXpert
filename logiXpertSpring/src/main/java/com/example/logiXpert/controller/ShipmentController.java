package com.example.logiXpert.controller;

import com.example.logiXpert.dto.GetAllShipmentDto;
import com.example.logiXpert.dto.GetShipmentDto;
import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.dto.UpdateStatusShipmentDto;
import com.example.logiXpert.service.ShipmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shipment")
//@PreAuthorize("!hasAuthority('CLIENT')")
public class ShipmentController {
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<GetShipmentDto> getShipmentById(@PathVariable("id") Integer id) {
        GetShipmentDto shipment = shipmentService.getShipmentById(id);
        return new ResponseEntity<>(shipment, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("!hasAuthority('COURIER')")
    public ResponseEntity<ShipmentDto> addShipment(@RequestBody ShipmentDto shipment) {
        ShipmentDto newShipment = shipmentService.addShipment(shipment);
        return new ResponseEntity<>(newShipment, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<GetShipmentDto> updateShipment(@RequestBody ShipmentDto shipment) {
        GetShipmentDto updateShipment = shipmentService.updateShipment(shipment);
        return new ResponseEntity<>(updateShipment, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OFFICE_EMPLOYEE')")
    public ResponseEntity<?> deleteShipment(@PathVariable("id") Integer id) {
        shipmentService.deleteShipment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetAllShipmentDto>> getAllShipments() {
        List<GetAllShipmentDto> shipments = shipmentService.getAllShipments();
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getRevenue(
            @RequestParam("companyId") Integer companyId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        double revenue = shipmentService.calculateTotalRevenueForPeriod(companyId, startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/not-delivered")
    public ResponseEntity<List<GetAllShipmentDto>> getNotDeliveredShipments() {
        List<GetAllShipmentDto> shipments = shipmentService.getNotDeliveredShipments();
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }

    @GetMapping("/track/{trackingNum}")
    public ResponseEntity<GetShipmentDto> getShipmentByTrackingNumber(@PathVariable("trackingNum") String trackingNum) {
        GetShipmentDto shipment = shipmentService.getShipmentByTrackingNumber(trackingNum);
        return new ResponseEntity<>(shipment, HttpStatus.OK);
    }

    @PutMapping("/update-status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GetShipmentDto> updateShipmentStatus(@RequestBody UpdateStatusShipmentDto uShipment) {
        GetShipmentDto updateShipment = shipmentService.updateShipmentStatus(uShipment);
        return new ResponseEntity<>(updateShipment, HttpStatus.CREATED);
    }

    @PutMapping("/{shipmentId}/assign-courier/{courierId}")
    //@PreAuthorize("hasAuthority('COURIER')")
    public ResponseEntity<GetShipmentDto> assignShipmentToCourier(
            @PathVariable("courierId") Integer courierId,
            @PathVariable("shipmentId") Integer shipmentId) {
        GetShipmentDto updatedShipment = shipmentService.assignShipmentToCourier(courierId, shipmentId);
        return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
    }
}
