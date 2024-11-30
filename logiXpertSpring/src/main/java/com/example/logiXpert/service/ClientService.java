package com.example.logiXpert.service;

import com.example.logiXpert.dto.ClientDto;
import com.example.logiXpert.model.Client;

public interface ClientService {
    ClientDto addClient(ClientDto clientDto);

    ClientDto updateClient(ClientDto clientDto);

    ClientDto getClientById(Integer id);

    void deleteClient(Integer id);
}