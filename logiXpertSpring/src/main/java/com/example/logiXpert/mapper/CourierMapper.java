package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.CourierDto;
import com.example.logiXpert.dto.RegisterCourierDto;
import com.example.logiXpert.dto.RegisterOfficeEmployeeDto;
import com.example.logiXpert.model.Courier;
import com.example.logiXpert.model.OfficeEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourierMapper {
    // Entity към DTO
    @Mapping(target = "officeName", expression = "java(courier.getOffice() != null ? courier.getOffice().getName() : null)")
    @Mapping(target = "companyName", expression = "java(courier.getCompany() != null ? courier.getCompany().getName() : null)")
    //@Mapping(target = "role", expression = "java(courier.getRoles().isEmpty() ? null : courier.getRoles().iterator().next().getName().toString())")
    CourierDto toDto(Courier courier);

    // DTO към Entity
    @Mapping(target = "office", ignore = true)
    //@Mapping(target = "roles", ignore = true)
    Courier toEntity(CourierDto courierDto);

    default Courier toEntity(RegisterCourierDto registrationDto) {
        Courier courier = new Courier();
        courier.setName(registrationDto.name());
        courier.setPhone(registrationDto.phone());
        courier.setEmail(registrationDto.email());
        courier.setSalary(registrationDto.salary());
        courier.setVehicleId(registrationDto.vehicleId());
        return courier;
    }
}
