package com.example.logiXpert.service;

import com.example.logiXpert.dto.CourierDto;
import com.example.logiXpert.dto.RegisterCourierDto;
import com.example.logiXpert.exception.CourierNotFoundException;
import com.example.logiXpert.exception.OfficeEmployeeNotFoundException;
import com.example.logiXpert.mapper.CourierMapper;
import com.example.logiXpert.model.Courier;
import com.example.logiXpert.model.ERole;
import com.example.logiXpert.model.Role;
import com.example.logiXpert.repository.CourierRepository;
import com.example.logiXpert.repository.OfficeRepository;
import com.example.logiXpert.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final CourierMapper courierMapper;
    private final OfficeRepository officeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CourierServiceImpl(
            CourierRepository courierRepository,
            OfficeRepository officeRepository,
            CourierMapper courierMapper,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.courierRepository = courierRepository;
        this.officeRepository = officeRepository;
        this.courierMapper = courierMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CourierDto addCourier(RegisterCourierDto registrationDto) {

        Courier courier = courierMapper.toEntity(registrationDto);
        courier.setPassword(passwordEncoder.encode(registrationDto.password()));

        courier.setOffice(
                officeRepository.findByName(registrationDto.officeName())
                        .orElseThrow(() -> new CourierNotFoundException("Office not found"))
        );

        courier.setCompany(
                courier.getOffice().getCompany()
        );


        Role role = roleRepository.findByName(ERole.COURIER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        courier.getRoles().add(role);
        Courier savedCourier = courierRepository.save(courier);
        return courierMapper.toDto(savedCourier);
    }

    @Override
    public CourierDto updateCourier(CourierDto courierDto) {

        Courier existingCourier = courierRepository.findById(courierDto.id())
                .orElseThrow(() -> new CourierNotFoundException("Courier with id " + courierDto.id() + " was not found"));

        existingCourier.setName(courierDto.name());
        existingCourier.setPhone(courierDto.phone());
        existingCourier.setEmail(courierDto.email());
        //existingCourier.setPassword(courierDto.password());

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

    @Override
    public CourierDto updateCourierByAdmin(CourierDto courierDto) {
        Courier existingCourier = courierRepository.findById(courierDto.id())
                .orElseThrow(() -> new CourierNotFoundException("Courier with id " + courierDto.id() + " was not found"));

        existingCourier.setSalary(courierDto.salary());
        existingCourier.setVehicleId(courierDto.vehicleId());

        existingCourier.setOffice(
                officeRepository.findByName(courierDto.officeName())
                        .orElseThrow(() -> new OfficeEmployeeNotFoundException("Office not found"))
        );

        existingCourier.setCompany(
                existingCourier.getOffice().getCompany()
        );

        Courier updatedCourier = courierRepository.save(existingCourier);
        return courierMapper.toDto(updatedCourier);

    }
}

