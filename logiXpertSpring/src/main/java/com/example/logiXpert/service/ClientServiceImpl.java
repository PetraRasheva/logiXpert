package com.example.logiXpert.service;

import com.example.logiXpert.exception.ClientNotFoundException;
import com.example.logiXpert.model.Client;
import com.example.logiXpert.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client getClientById(Integer id) {
        return clientRepository.findClientById(id).orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " was not found"));
    }

    @Override
    public void deleteClient(Integer id) {
        clientRepository.deleteClientById(id);
    }

    //TODO: Implement complex requests
}
