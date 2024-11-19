package com.example.logiXpert.service;

import com.example.logiXpert.exception.AdminNotFoundException;
import com.example.logiXpert.model.Admin;
import com.example.logiXpert.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    @Override
    public Admin addAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Integer id) {
        return adminRepository.findAdminById(id).orElseThrow(() -> new AdminNotFoundException("Admin with id " + id + " was not found"));
    }

    @Override
    public void deleteAdmin(Integer id) {
        adminRepository.deleteAdminById(id);
    }

    //TODO: Implement complex requests
}
