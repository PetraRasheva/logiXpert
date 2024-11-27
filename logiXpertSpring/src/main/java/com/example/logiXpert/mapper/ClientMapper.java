package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.ClientDto;
import com.example.logiXpert.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDto toDto(Client client);       // Мапва Entity към DTO
    Client toEntity(ClientDto clientDto); // Мапва DTO към Entity
}