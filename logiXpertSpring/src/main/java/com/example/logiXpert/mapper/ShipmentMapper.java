package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.model.Shipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    ShipmentDto toDto(Shipment shipment);
    Shipment toEntity(ShipmentDto shipmentDto);
}
