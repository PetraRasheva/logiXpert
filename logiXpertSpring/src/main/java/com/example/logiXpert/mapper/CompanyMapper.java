package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.model.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyDto toDto(Company company);
    Company toEntity(CompanyDto companyDto);
}
