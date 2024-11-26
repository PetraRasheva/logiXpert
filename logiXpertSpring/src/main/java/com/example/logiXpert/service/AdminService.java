package com.example.logiXpert.service;

import com.example.logiXpert.dto.AdminDto;
import com.example.logiXpert.model.Admin;

public interface AdminService {
    AdminDto addAdmin(AdminDto admin);

    AdminDto updateAdmin(AdminDto admin);

    AdminDto getAdminById(Integer id);

    void deleteAdmin(Integer id);
}