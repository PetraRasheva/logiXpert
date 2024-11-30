package com.example.logiXpert.service;

import com.example.logiXpert.dto.CourierDto;
import com.example.logiXpert.exception.CourierNotFoundException;
import com.example.logiXpert.mapper.CourierMapper;
import com.example.logiXpert.model.Courier;
import com.example.logiXpert.repository.CourierRepository;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.OfficeRepository;
import com.example.logiXpert.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final CourierMapper courierMapper;
    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public CourierServiceImpl(
            CourierRepository courierRepository, OfficeRepository officeRepository,
            CompanyRepository companyRepository, CourierMapper courierMapper) {

        this.courierRepository = courierRepository;
        this.officeRepository = officeRepository;
        this.companyRepository = companyRepository;
        this.courierMapper = courierMapper;
    }

    @Override
    public CourierDto addCourier(CourierDto courierDto) {
        Courier courier = courierMapper.toEntity(courierDto);

        courier.setOffice(
                officeRepository.findByName(courierDto.officeName())
                        .orElseThrow(() -> new CourierNotFoundException("Office not found"))
        );

        courier.setCompany(
                companyRepository.findByName(courierDto.companyName())
                        .orElseThrow(() -> new CourierNotFoundException("Company not found"))
        );

        Courier savedCourier = courierRepository.save(courier);
        return courierMapper.toDto(savedCourier);
    }

    @Override
    public CourierDto updateCourier(CourierDto courierDto) {

        Courier existingCourier = courierRepository.findById(courierDto.id())
                .orElseThrow(() -> new CourierNotFoundException("Courier with id " + courierDto.id() + " was not found"));

        existingCourier.setName(courierDto.name());
        existingCourier.setPhone(courierDto.phone());
        existingCourier.setSalary(courierDto.salary());
        existingCourier.setEmail(courierDto.email());
        existingCourier.setVehicleId(courierDto.vehicleId());

//        existingCourier.setOffice(
//                officeRepository.findByName(courierDto.officeName())
//                        .orElseThrow(() -> new CourierNotFoundException("Office not found"))
//        );
//
//        existingCourier.setCompany(
//                companyRepository.findByName(courierDto.companyName())
//                        .orElseThrow(() -> new CourierNotFoundException("Company not found"))
//        );


        Courier updatedCourier = courierRepository.save(existingCourier);
        return courierMapper.toDto(updatedCourier);

    }

    @Override
    public CourierDto getCourierById(Integer id) {
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new CourierNotFoundException("Courier with id " + id + " was not found"));

        return courierMapper.toDto(courier);
    }

    @Override
    public void deleteCourier(Integer id) {
        if (!courierRepository.existsById(id)) {
            throw new CourierNotFoundException("Courier with id " + id + " was not found");
        }

        courierRepository.deleteCourierById(id);
    }

    //TODO: Implement complex requests
}

