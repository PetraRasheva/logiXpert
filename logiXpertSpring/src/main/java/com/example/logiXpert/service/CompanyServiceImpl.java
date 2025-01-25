package com.example.logiXpert.service;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.dto.GetUserDto;
import com.example.logiXpert.exception.CompanyNotFoundException;
import com.example.logiXpert.mapper.CompanyMapper;
import com.example.logiXpert.model.*;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.ShipmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserService userService;
    private final ShipmentRepository shipmentRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper, UserService userService, ShipmentRepository shipmentRepository) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.userService = userService;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public CompanyDto addCompany(CompanyDto companyDto) {
        Company company = companyRepository.save(companyMapper.toEntity(companyDto));
        return companyMapper.toDto(company);
    }

    @Override
    public CompanyDto updateCompany(CompanyDto companyDto) {
        Company company = companyRepository.findById(companyDto.id())
                .orElseThrow(() -> new CompanyNotFoundException("Company with id " + companyDto.id() + " was not found"));
        company.setName(companyDto.name());
        company.setBaseCapital(companyDto.baseCapital());
        company.setAddressFee(companyDto.addressFee());
        return companyMapper.toDto(companyRepository.save(company));
    }

    @Override
    public CompanyDto getCompanyById(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company with id " + id + " was not found"));
        return companyMapper.toDto(company);
    }

    @Override
    @Transactional
    public void deleteCompany(Integer id) {
        companyRepository.deleteCompanyById(id);
    }

    @Override
    public List<GetUserDto> getAllEmployees() {
        Set<ERole> roles = Set.of(ERole.COURIER, ERole.OFFICE_EMPLOYEE);
        return userService.getAllWithRoles(roles);
    }

    @Override
    public double calculateRevenue(Integer companyId) {
        // Ensure the company exists
        companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company with ID " + companyId + " not found"));

        // Calculate revenue and handle null safely
        Double revenue = shipmentRepository.calculateRevenueByCompanyId(companyId);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public double calculateRevenueDateRange(Integer companyId, LocalDateTime startDate, LocalDateTime endDate) {
        // Ensure the company exists
        companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company with ID " + companyId + " not found"));

        // Calculate revenue and handle null safely
        Double revenue = shipmentRepository.calculateRevenueByCompanyAndDateRange(companyId, startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }
}
