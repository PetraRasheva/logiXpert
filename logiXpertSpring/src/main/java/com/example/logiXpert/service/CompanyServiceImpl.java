package com.example.logiXpert.service;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.exception.CompanyNotFoundException;
import com.example.logiXpert.exception.CourierNotFoundException;
import com.example.logiXpert.exception.ShipmentNotFoundException;
import com.example.logiXpert.mapper.CompanyMapper;
import com.example.logiXpert.model.Company;
import com.example.logiXpert.model.Courier;
import com.example.logiXpert.model.Shipment;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.CourierRepository;
import com.example.logiXpert.repository.ShipmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CourierRepository courierRepository;
    private final ShipmentRepository shipmentRepository;
    private final CompanyMapper companyMapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CourierRepository courierRepository, ShipmentRepository shipmentRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.courierRepository = courierRepository;
        this.shipmentRepository = shipmentRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public CompanyDto addCompany(CompanyDto companyDto) {
        Company company = companyRepository.save(companyMapper.toEntity(companyDto));
        return companyMapper.toDto(company);
    }

    @Override
    public CompanyDto updateCompanyById(Integer id, CompanyDto companyDto) {
        Company updateCompany = companyMapper.toEntity(companyDto);
        Company company = companyRepository.findCompanyById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company with id " + id + " was not found"));
        company.setName(updateCompany.getName());
        company.setBaseCapital(updateCompany.getBaseCapital());
        Company updatedCompany = companyRepository.save(company);
        return companyMapper.toDto(updatedCompany);
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

    @Override
    @Transactional
    public void assignShipmentToCourier(int shipmentId, int courierId) {
        Shipment shipment = shipmentRepository.findShipmentById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + shipmentId + " was not found"));;
        Courier courier = courierRepository.findCourierById(courierId).orElseThrow(() -> new CourierNotFoundException("Courier with id " + courierId + " was not found"));;

        // Remove shipment from all other couriers
        courierRepository.findAll().forEach(c -> {
            if (c.getAssignedShipments().contains(shipment)) {
                c.unassignShipment(shipment);
                courierRepository.save(c); // Save changes
            }
        });

        // Assign shipment to the target courier
        courier.assignShipment(shipment);
        courierRepository.save(courier);  // Persist assignment
        System.out.println("Assigned shipment " + shipmentId + " to courier " + courierId);
    }
}
