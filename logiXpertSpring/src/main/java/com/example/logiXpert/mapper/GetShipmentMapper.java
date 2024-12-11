package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.GetShipmentDto;
import com.example.logiXpert.dto.ShipmentDto;
import com.example.logiXpert.model.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GetShipmentMapper {

    @Mapping(target = "courierName", expression = "java(shipment.getCourier() != null ? shipment.getCourier().getName() : null)")
    GetShipmentDto toDto(Shipment shipment);
    Shipment toEntity(ShipmentDto shipmentDto);
}
