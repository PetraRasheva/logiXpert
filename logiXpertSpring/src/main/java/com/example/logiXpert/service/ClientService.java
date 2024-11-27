package com.example.logiXpert.service;

import com.example.logiXpert.dto.ClientDto;
import com.example.logiXpert.model.Client;

public interface ClientService {
    ClientDto addClient(ClientDto clientDto); // Приема DTO за добавяне

    ClientDto updateClient(ClientDto clientDto); // Приема DTO за актуализация

    ClientDto getClientById(Integer id); // Връща DTO при търсене по ID

    void deleteClient(Integer id); // Изтрива клиент по ID
}