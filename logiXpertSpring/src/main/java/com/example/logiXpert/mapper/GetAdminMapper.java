package com.example.logiXpert.mapper;

import com.example.logiXpert.dto.AdminDto;
import com.example.logiXpert.dto.GetAdminDto;
import com.example.logiXpert.model.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GetAdminMapper {
    GetAdminDto toDto(Admin admin);
    Admin toEntity(AdminDto adminDto);
}
