package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.OfficeEmployeeRegistrationDto;
import com.example.logiXpert.model.OfficeEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfficeEmployeeMapper {

    @Mapping(target = "officeName", expression = "java(officeEmployee.getOffice() != null ? officeEmployee.getOffice().getName() : null)")
    @Mapping(target = "companyName", expression = "java(officeEmployee.getCompany() != null ? officeEmployee.getCompany().getName() : null)")
    @Mapping(target = "role", expression = "java(officeEmployee.getRoles().isEmpty() ? null : officeEmployee.getRoles().iterator().next().getName().toString())")
    OfficeEmployeeDto toDto(OfficeEmployee officeEmployee);

    @Mapping(target = "office", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "roles", ignore = true)
    OfficeEmployee toEntity(OfficeEmployeeDto officeEmployeeDto);

    default OfficeEmployee toEntity(OfficeEmployeeRegistrationDto registrationDto) {
        OfficeEmployee employee = new OfficeEmployee();
        employee.setName(registrationDto.name());
        employee.setPhone(registrationDto.phone());
        employee.setEmail(registrationDto.email());
        employee.setSalary(registrationDto.salary());
        return employee;
    }
}