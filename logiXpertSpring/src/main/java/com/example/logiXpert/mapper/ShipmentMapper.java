package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.GetAllShipmentDto;
import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.model.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    @Mapping(target = "ownerId", expression = "java(shipment.getOwner() != null ? shipment.getOwner().getId() : null)")
    @Mapping(target = "companyId", expression = "java(shipment.getCompany() != null ? shipment.getCompany().getId() : null)")
    ShipmentDto toDto(Shipment shipment);

    GetAllShipmentDto toGetAllDto(Shipment shipment);

    Shipment toEntity(ShipmentDto shipmentDto);
}