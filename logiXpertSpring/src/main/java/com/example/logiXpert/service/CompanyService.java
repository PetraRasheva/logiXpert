package com.example.logiXpert.service;

import com.example.logiXpert.exception.CompanyNotFoundException;
import com.example.logiXpert.model.Company;

public interface CompanyService {
    Company addCompany(Company company);

    Company updateCompany(Company company);

    Company getCompanyById(Integer id);

    void deleteCompany(Integer id);

    void assignShipmentToCourier(int shipmentId, int courierId);
}
