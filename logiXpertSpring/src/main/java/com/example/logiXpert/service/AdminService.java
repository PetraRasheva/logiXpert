package com.example.logiXpert.service;

import com.example.logiXpert.model.Admin;

public interface AdminService {
    Admin addAdmin(Admin client);

    Admin updateAdmin(Admin client);

    Admin getAdminById(Integer id);

    void deleteAdmin(Integer id);
}
