package com.example.logiXpert.service;

import com.example.logiXpert.dto.AdminDto;
import com.example.logiXpert.dto.GetAdminDto;
import com.example.logiXpert.model.Admin;

public interface AdminService {
    AdminDto addAdmin(AdminDto admin);

    GetAdminDto updateAdmin(AdminDto admin);

    GetAdminDto getAdminById(Integer id);

    void deleteAdmin(Integer id);
}