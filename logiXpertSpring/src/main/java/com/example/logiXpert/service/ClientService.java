package com.example.logiXpert.service;

import com.example.logiXpert.dto.ClientDto;
import com.example.logiXpert.dto.GetAllShipmentDto;

import java.util.List;

public interface ClientService {
    ClientDto addClient(ClientDto clientDto);

    ClientDto updateClient(ClientDto clientDto);

    ClientDto getClientById(Integer id);

    void deleteClient(Integer id);

    List<ClientDto> getAllClients() ;
    
    List<GetAllShipmentDto> getSentShipments(int clientId);

    List<GetAllShipmentDto> getReceivedShipments(int clientId);
}