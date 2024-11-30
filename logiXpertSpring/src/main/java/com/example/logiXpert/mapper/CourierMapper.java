package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.CourierDto;
import com.example.logiXpert.model.Courier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourierMapper {
    // Entity към DTO
    @Mapping(target = "officeName", expression = "java(courier.getOffice() != null ? courier.getOffice().getName() : null)")
    @Mapping(target = "companyName", expression = "java(courier.getCompany() != null ? courier.getCompany().getName() : null)")
    CourierDto toDto(Courier courier);

    // DTO към Entity
    @Mapping(target = "office", ignore = true)
    @Mapping(target = "company", ignore = true)
    Courier toEntity(CourierDto courierDto);
}
