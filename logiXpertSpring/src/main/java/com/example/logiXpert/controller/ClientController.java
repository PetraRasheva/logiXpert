package com.example.logiXpert.controller;

import com.example.logiXpert.dto.ClientDto;
import com.example.logiXpert.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") Integer id) {
        ClientDto clientDto = clientService.getClientById(id); // Извиква сервиз с DTO
        return new ResponseEntity<>(clientDto, HttpStatus.OK); // Връща DTO
    }

    @PostMapping("/add")
    public ResponseEntity<ClientDto> addClient(@RequestBody ClientDto clientDto) {
        ClientDto newClient = clientService.addClient(clientDto); // Използва DTO
        return new ResponseEntity<>(newClient, HttpStatus.CREATED); // Връща DTO
    }

    @PutMapping("/update")
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) {
        ClientDto updatedClient = clientService.updateClient(clientDto); // Използва DTO
        return new ResponseEntity<>(updatedClient, HttpStatus.OK); // Връща DTO
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Integer id) {
        clientService.deleteClient(id); // Извиква сервиз за изтриване
        return new ResponseEntity<>(HttpStatus.OK); // Връща само статус
    }
}