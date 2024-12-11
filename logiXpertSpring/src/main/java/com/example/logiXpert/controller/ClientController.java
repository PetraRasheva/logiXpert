package com.example.logiXpert.controller;

import com.example.logiXpert.dto.ClientDto;
import com.example.logiXpert.dto.GetAllShipmentDto;
import com.example.logiXpert.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OFFICE_EMPLOYEE')")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") Integer id) {
        ClientDto clientDto = clientService.getClientById(id);
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OFFICE_EMPLOYEE')")
    public ResponseEntity<ClientDto> addClient(@RequestBody ClientDto clientDto) {
        ClientDto newClient = clientService.addClient(clientDto);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ClientDto> updateClient(@Valid @RequestBody ClientDto clientDto) {
        ClientDto updatedClient = clientService.updateClient(clientDto);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OFFICE_EMPLOYEE')")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Integer id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('OFFICE_EMPLOYEE')")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/{clientId}/sent-shipments")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<GetAllShipmentDto>> getSentShipments(@PathVariable int clientId) {
        List<GetAllShipmentDto> shipments = clientService.getSentShipments(clientId);
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/{clientId}/received-shipments")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<GetAllShipmentDto>> getReceivedShipments(@PathVariable int clientId) {
        List<GetAllShipmentDto> shipments = clientService.getReceivedShipments(clientId);
        return ResponseEntity.ok(shipments);
    }
}