package com.example.logiXpert.service;

import com.example.logiXpert.dto.AdminDto;
import com.example.logiXpert.exception.AdminNotFoundException;
import com.example.logiXpert.mapper.AdminMapper;
import com.example.logiXpert.model.Admin;
import com.example.logiXpert.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public AdminServiceImpl(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    @Override
    public AdminDto addAdmin(AdminDto adminDto) {
        Admin admin = adminMapper.toEntity(adminDto);
        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }

    @Override
    public AdminDto updateAdmin(AdminDto adminDto) {
        if (!adminRepository.existsById(adminDto.id())) {
            throw new AdminNotFoundException("Admin with id " + adminDto.id() + " was not found");
        }
        Admin admin = adminMapper.toEntity(adminDto);
        Admin updatedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(updatedAdmin);
    }

    @Override
    public AdminDto getAdminById(Integer id) {
        Admin admin = adminRepository.findAdminById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin with id " + id + " was not found"));
        return adminMapper.toDto(admin);
    }

    @Override
    @Transactional
    public void deleteAdmin(Integer id) {
        if (!adminRepository.existsById(id)) {
            throw new AdminNotFoundException("Admin with id " + id + " was not found");
        }
        adminRepository.deleteAdminById(id);
    }

}