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
        if (!adminRepository.existsById(admin.getId())) {
            throw new AdminNotFoundException("Admin with id " + admin.getId() + " was not found");
        }
        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Integer id) {
        return adminRepository.findAdminById(id).orElseThrow(() -> new AdminNotFoundException("Admin with id " + id + " was not found"));
    }

    // Add condition to check if admin exists before deleting
    @Override
    public void deleteAdmin(Integer id) {
        if (!adminRepository.existsById(id)) {
            throw new AdminNotFoundException("Admin with id " + id + " was not found");
        }
        adminRepository.deleteAdminById(id);
    }

    //TODO: Implement complex requests
}
