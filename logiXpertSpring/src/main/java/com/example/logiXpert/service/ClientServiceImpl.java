package com.example.logiXpert.service;

import com.example.logiXpert.dto.ClientDto;
import com.example.logiXpert.exception.ClientNotFoundException;
import com.example.logiXpert.mapper.ClientMapper;
import com.example.logiXpert.model.Client;
import com.example.logiXpert.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
        Client client = clientMapper.toEntity(clientDto);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }

    @Override
    @Transactional
    public ClientDto updateClient(ClientDto clientDto) {
        Client existingClient = clientRepository.findById(clientDto.id())
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        existingClient.setName(clientDto.name());
        existingClient.setPhone(clientDto.phone());
        existingClient.setEmail(clientDto.email());

        return clientMapper.toDto(existingClient);
    }

    @Override
    public ClientDto getClientById(Integer id) {
        Client client = clientRepository.findClientById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " was not found"));
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional
    public void deleteClient(Integer id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client with id " + id + " was not found");
        }
        clientRepository.deleteClientById(id);
    }

    @Override
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(clientMapper::toDto).toList();
    }
}