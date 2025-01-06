package com.example.logiXpert.service;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.dto.GetUserDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CompanyService {
    CompanyDto addCompany(CompanyDto company);

    CompanyDto updateCompany(CompanyDto company);

    CompanyDto getCompanyById(Integer id);

    void deleteCompany(Integer id);

    List<GetUserDto> getAllEmployees();

    double calculateRevenue(Integer id);

    double calculateRevenueDateRange(Integer id, LocalDateTime startDate, LocalDateTime endDate);
}
