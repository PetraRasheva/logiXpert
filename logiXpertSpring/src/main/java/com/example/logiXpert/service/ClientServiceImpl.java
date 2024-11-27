package com.example.logiXpert.service;

import com.example.logiXpert.dto.ClientDto;
import com.example.logiXpert.exception.ClientNotFoundException;
import com.example.logiXpert.mapper.ClientMapper;
import com.example.logiXpert.mapper.UserMapper;
import com.example.logiXpert.model.Client;
import com.example.logiXpert.repository.ClientRepository;
import com.example.logiXpert.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientDto addClient(ClientDto clientDto) {
        Client client = clientMapper.toEntity(clientDto); // DTO -> Entity
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient); // Entity -> DTO
    }

    @Override
    public ClientDto updateClient(ClientDto clientDto) {
        if (!clientRepository.existsById(clientDto.id())) {
            throw new ClientNotFoundException("Client with id " + clientDto.id() + " was not found");
        }
        Client client = clientMapper.toEntity(clientDto); // DTO -> Entity
        Client updatedClient = clientRepository.save(client);
        return clientMapper.toDto(updatedClient); // Entity -> DTO
    }

    @Override
    public ClientDto getClientById(Integer id) {
        Client client = clientRepository.findClientById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " was not found"));
        return clientMapper.toDto(client); // Entity -> DTO
    }

    @Override
    @Transactional
    public void deleteClient(Integer id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client with id " + id + " was not found");
        }
        clientRepository.deleteClientById(id);
    }
}