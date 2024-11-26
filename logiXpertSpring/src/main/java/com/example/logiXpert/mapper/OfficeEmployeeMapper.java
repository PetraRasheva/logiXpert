package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.model.OfficeEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfficeEmployeeMapper {

    // Entity към DTO
    @Mapping(target = "officeName", expression = "java(officeEmployee.getOffice() != null ? officeEmployee.getOffice().getName() : null)")
    @Mapping(target = "companyName", expression = "java(officeEmployee.getCompany() != null ? officeEmployee.getCompany().getName() : null)")
    OfficeEmployeeDto toDto(OfficeEmployee officeEmployee);

    // DTO към Entity
    @Mapping(target = "office", ignore = true)
    @Mapping(target = "company", ignore = true)
    OfficeEmployee toEntity(OfficeEmployeeDto officeEmployeeDto);
}