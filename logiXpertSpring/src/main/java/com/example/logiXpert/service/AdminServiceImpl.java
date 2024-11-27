package com.example.logiXpert.service;

import com.example.logiXpert.dto.AdminDto;
import com.example.logiXpert.dto.GetAdminDto;
import com.example.logiXpert.exception.AdminNotFoundException;
import com.example.logiXpert.mapper.AdminMapper;
import com.example.logiXpert.mapper.GetAdminMapper;
import com.example.logiXpert.model.Admin;
import com.example.logiXpert.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final GetAdminMapper getAdminMapper;

    public AdminServiceImpl(AdminRepository adminRepository, AdminMapper adminMapper, GetAdminMapper getAdminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
        this.getAdminMapper = getAdminMapper;
    }

    @Override
    public AdminDto addAdmin(AdminDto adminDto) {
        Admin admin = adminMapper.toEntity(adminDto);
        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }

    @Override
    public GetAdminDto updateAdmin(AdminDto adminDto) {
        if (!adminRepository.existsById(adminDto.id())) {
            throw new AdminNotFoundException("Admin with id " + adminDto.id() + " was not found");
        }
        Admin admin = getAdminMapper.toEntity(adminDto);
        Admin updatedAdmin = adminRepository.save(admin);
        return getAdminMapper.toDto(updatedAdmin);
    }

    @Override
    public GetAdminDto getAdminById(Integer id) {
        Admin admin = adminRepository.findAdminById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin with id " + id + " was not found"));
        return getAdminMapper.toDto(admin);
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