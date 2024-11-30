package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.OfficeDto;
import com.example.logiXpert.model.Office;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfficeMapper {

    // Entity към DTO
    @Mapping(target = "companyName", expression = "java(office.getCompany() != null ? office.getCompany().getName() : null)")
    OfficeDto toDto(Office office);

    // DTO към Entity
    @Mapping(target = "company", ignore = true)
    Office toEntity(OfficeDto officeDto);
}
