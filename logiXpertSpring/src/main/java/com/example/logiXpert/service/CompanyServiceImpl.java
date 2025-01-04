package com.example.logiXpert.service;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.dto.GetUserDto;
import com.example.logiXpert.exception.CompanyNotFoundException;
import com.example.logiXpert.mapper.CompanyMapper;
import com.example.logiXpert.model.*;
import com.example.logiXpert.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserService userService;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper, UserService userService) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.userService = userService;
    }

    @Override
    public CompanyDto addCompany(CompanyDto companyDto) {
        Company company = companyRepository.save(companyMapper.toEntity(companyDto));
        return companyMapper.toDto(company);
    }

    @Override
    public CompanyDto updateCompany(CompanyDto companyDto) {
        Company company = companyRepository.findCompanyById(companyDto.id())
                .orElseThrow(() -> new CompanyNotFoundException("Company with id " + companyDto.id() + " was not found"));
        company.setName(companyDto.name());
        company.setBaseCapital(companyDto.baseCapital());
        return companyMapper.toDto(companyRepository.save(company));
    }

    @Override
    public CompanyDto getCompanyById(Integer id) {
        Company company = companyRepository.findCompanyById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company with id " + id + " was not found"));
        return companyMapper.toDto(company);
    }

    @Override
    public void deleteCompany(Integer id) {
        companyRepository.deleteCompanyById(id);
    }

    //TODO: Implement complex requests

    //TODO: Courier should assign select which unassigned shipments to assign to himself

    @Override
    public List<GetUserDto> getAllEmployees() {
        Set<ERole> roles = Set.of(ERole.COURIER, ERole.OFFICE_EMPLOYEE);
        return userService.getAllWithRoles(roles);
    }
}
