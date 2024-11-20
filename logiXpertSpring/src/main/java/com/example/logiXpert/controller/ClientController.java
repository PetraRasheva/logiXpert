package com.example.logiXpert.controller;

import com.example.logiXpert.model.Client;
import com.example.logiXpert.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Client> getClientById (@PathVariable("id") Integer id) {
        Client client = clientService.getClientById(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        Client newClient = clientService.addClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        Client updateClient = clientService.updateClient(client);
        return new ResponseEntity<>(updateClient, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Integer id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}