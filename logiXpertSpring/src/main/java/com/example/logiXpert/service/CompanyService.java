package com.example.logiXpert.service;

import com.example.logiXpert.exception.CompanyNotFoundException;
import com.example.logiXpert.exception.CourierNotFoundException;
import com.example.logiXpert.exception.ShipmentNotFoundException;
import com.example.logiXpert.model.Company;
import com.example.logiXpert.model.Courier;
import com.example.logiXpert.model.Shipment;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.CourierRepository;
import com.example.logiXpert.repository.ShipmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CourierRepository courierRepository;
    private final ShipmentRepository shipmentRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, CourierRepository courierRepository, ShipmentRepository shipmentRepository) {
        this.companyRepository = companyRepository;
        this.courierRepository = courierRepository;
        this.shipmentRepository = shipmentRepository;
    }

    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company getCompanyById(Integer id) {
        return companyRepository.findCompanyById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company with id " + id + " was not found"));
    }

    public void deleteCompany(Integer id) {
        companyRepository.deleteCompanyById(id);
    }

    @Transactional
    public void assignShipmentToCourier(int shipmentId, int courierId) {
        Shipment shipment = shipmentRepository.findShipmentBy(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + shipmentId + " was not found"));;
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
