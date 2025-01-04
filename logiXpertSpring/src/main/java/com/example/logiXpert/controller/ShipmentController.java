package com.example.logiXpert.controller;

import com.example.logiXpert.dto.GetAllShipmentDto;
import com.example.logiXpert.dto.GetShipmentDto;
import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.dto.UpdateStatusShipmentDto;
import com.example.logiXpert.service.ShipmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> addShipment(@RequestBody ShipmentDto shipment) {
        ShipmentDto newShipment = shipmentService.addShipment(shipment);

        String pdfDownloadLink = newShipment.id() + "/invoice";

        String fullLink = "http://localhost:8080/shipment/" + pdfDownloadLink;

        Map<String, Object> response = new HashMap<>();
        response.put("shipment", newShipment);
        response.put("pdfDownloadLink", fullLink);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{shipmentId}/invoice")
    public ResponseEntity<byte[]> downloadShipmentInvoice(@PathVariable Integer shipmentId) {
        byte[] pdf = shipmentService.getShipmentInvoice(shipmentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=shipment_invoice_" + shipmentId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/employee/{employeeId}/shipments")
    public ResponseEntity<List<GetAllShipmentDto>> getShipmentsCreatedByEmployee(@PathVariable("employeeId") Integer employeeId) {
        List<GetAllShipmentDto> shipments = shipmentService.getShipmentsCreatedByEmployee(employeeId);
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/client/{clientId}/shipments")
    public ResponseEntity<List<GetAllShipmentDto>> getShipmentsCreatedByClient(@PathVariable("clientId") Integer clientId) {
        List<GetAllShipmentDto> shipments = shipmentService.getShipmentsCreatedByClient(clientId);
        return ResponseEntity.ok(shipments);
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

    @GetMapping("/revenueByDateRange")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Double> getRevenue(
            @RequestParam("companyId") Integer companyId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        double revenue = shipmentService.calculateTotalRevenueForPeriod(companyId, startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/totalRevenue")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Double> getTotalRevenue(
            @RequestParam("companyId") Integer companyId) {
        double revenue = shipmentService.calculateCompanyRevenue(companyId);
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

    @GetMapping("/courier/{courierId}")
    public ResponseEntity<List<GetAllShipmentDto>> getShipmentsByCourierId(@PathVariable("courierId") Integer courierId) {
        List<GetAllShipmentDto> shipments = shipmentService.getShipmentsByCourierId(courierId);
        return ResponseEntity.ok(shipments);
    }

    @PutMapping("/{shipmentId}/assign-courier/{courierId}")
    //@PreAuthorize("hasAuthority('COURIER')")
    public ResponseEntity<GetShipmentDto> assignShipmentToCourier(
            @PathVariable("courierId") Integer courierId,
            @PathVariable("shipmentId") Integer shipmentId) {
        GetShipmentDto updatedShipment = shipmentService.assignShipmentToCourier(courierId, shipmentId);
        return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
    }

    @PutMapping("/{shipmentId}/unassign-courier")
    public ResponseEntity<GetShipmentDto> unassignShipmentFromCourier(@PathVariable("shipmentId") Integer shipmentId) {
        GetShipmentDto updatedShipment = shipmentService.unassignShipmentFromCourier(shipmentId);
        return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
    }

    @PutMapping("/unassign-by-tracking/{trackingNumber}")
    public ResponseEntity<GetShipmentDto> unassignShipmentFromCourierByTrackingNumber(
            @PathVariable("trackingNumber") String trackingNumber) {
        GetShipmentDto updatedShipment = shipmentService.unassignShipmentFromCourierByTrackingNumber(trackingNumber);
        return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
    }

    @GetMapping("/courier/{courierId}/unassigned-shipments")
    public ResponseEntity<List<GetAllShipmentDto>> getUnassignedShipmentsForCourier(@PathVariable("courierId") Integer courierId) {
        List<GetAllShipmentDto> unassignedShipments = shipmentService.getUnassignedShipmentsForCourier(courierId);
        return ResponseEntity.ok(unassignedShipments);
    }

    @PutMapping("/assign-by-tracking/{trackingNumber}/courier/{courierId}")
    public ResponseEntity<GetShipmentDto> assignShipmentToCourierByTrackingNumber(
            @PathVariable("trackingNumber") String trackingNumber,
            @PathVariable("courierId") Integer courierId) {
        GetShipmentDto updatedShipment = shipmentService.assignShipmentToCourierByTrackingNumber(trackingNumber, courierId);
        return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
    }
}
