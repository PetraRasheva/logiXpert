package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.model.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    @Mapping(source = "company.id", target = "companyId")
    ShipmentDto toDto(Shipment shipment);

    @Mapping(source = "companyId", target = "company.id")
    Shipment toEntity(ShipmentDto shipmentDto);
}