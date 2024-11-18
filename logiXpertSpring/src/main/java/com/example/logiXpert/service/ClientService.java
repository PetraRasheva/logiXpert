package com.example.logiXpert.service;

import com.example.logiXpert.model.Client;

public interface ClientService {
    Client addClient(Client client);

    Client updateClient(Client client);

    Client getClientById(Integer id);

    void deleteClient(Integer id);
}
