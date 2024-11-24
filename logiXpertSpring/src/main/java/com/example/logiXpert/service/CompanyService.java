package com.example.logiXpert.service;

import com.example.logiXpert.dto.CompanyDto;

public interface CompanyService {
    CompanyDto addCompany(CompanyDto company);

    CompanyDto updateCompanyById(Integer id, CompanyDto company);

    CompanyDto getCompanyById(Integer id);

    void deleteCompany(Integer id);

    void assignShipmentToCourier(int shipmentId, int courierId);
}
