package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.exception.CompanyNotFoundException;
import com.example.logiXpert.exception.OfficeEmployeeNotFoundException;
import com.example.logiXpert.mapper.OfficeEmployeeMapper;
import com.example.logiXpert.model.ERole;
import com.example.logiXpert.model.OfficeEmployee;
import com.example.logiXpert.model.Role;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.OfficeEmployeeRepository;
import com.example.logiXpert.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.logiXpert.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class OfficeEmployeeServiceImpl implements OfficeEmployeeService {

    private final OfficeEmployeeRepository officeEmployeeRepository;
    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;
    private final OfficeEmployeeMapper officeEmployeeMapper;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OfficeEmployeeServiceImpl(
            OfficeEmployeeRepository officeEmployeeRepository,
            OfficeRepository officeRepository,
            CompanyRepository companyRepository,
            OfficeEmployeeMapper officeEmployeeMapper,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.officeEmployeeRepository = officeEmployeeRepository;
        this.officeRepository = officeRepository;
        this.companyRepository = companyRepository;
        this.officeEmployeeMapper = officeEmployeeMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OfficeEmployeeDto addOfficeEmployee(OfficeEmployeeRegistrationDto registrationDto) {

        OfficeEmployee employee = officeEmployeeMapper.toEntity(registrationDto);
        employee.setPassword(passwordEncoder.encode(registrationDto.password()));

        employee.setOffice(
                officeRepository.findById(officeEmployeeDto.id())
                        .orElseThrow(() -> new OfficeEmployeeNotFoundException("Office not found"))
        );

        employee.setCompany(
                companyRepository.findById(officeEmployeeDto.id())
                        .orElseThrow(() -> new CompanyNotFoundException("Company not found"))
        );

        Role role = roleRepository.findByName(ERole.valueOf(registrationDto.role()))
                .orElseThrow(() -> new RuntimeException("Role not found"));

        employee.getRoles().add(role);
        OfficeEmployee savedEmployee = officeEmployeeRepository.save(employee);
        return officeEmployeeMapper.toDto(savedEmployee);
    }
    @Override
    public OfficeEmployeeDto updateOfficeEmployee(OfficeEmployeeDto officeEmployeeDto) {
        OfficeEmployee existingEmployee = officeEmployeeRepository.findById(officeEmployeeDto.id())
                .orElseThrow(() -> new OfficeEmployeeNotFoundException("OfficeEmployee with id " + officeEmployeeDto.id() + " was not found"));

        existingEmployee.setName(officeEmployeeDto.name());
        existingEmployee.setPhone(officeEmployeeDto.phone());
        existingEmployee.setSalary(officeEmployeeDto.salary());

//        existingEmployee.setOffice(
//                officeRepository.findByName(officeEmployeeDto.officeName())
//                        .orElseThrow(() -> new OfficeEmployeeNotFoundException("Office not found"))
//        );
//
//        existingEmployee.setCompany(
//                companyRepository.findByName(officeEmployeeDto.companyName())
//                        .orElseThrow(() -> new OfficeEmployeeNotFoundException("Company not found"))
//        );

        OfficeEmployee updatedEmployee = officeEmployeeRepository.save(existingEmployee);

        return officeEmployeeMapper.toDto(updatedEmployee);
    }

    @Override
    public OfficeEmployeeDto getOfficeEmployeeById(Integer id) {
        OfficeEmployee employee = officeEmployeeRepository.findById(id)
                .orElseThrow(() -> new OfficeEmployeeNotFoundException("OfficeEmployee with id " + id + " was not found"));
        return officeEmployeeMapper.toDto(employee);
    }

    @Override
    public void deleteOfficeEmployee(Integer id) {
        if (!officeEmployeeRepository.existsById(id)) {
            throw new OfficeEmployeeNotFoundException("OfficeEmployee with id " + id + " was not found");
        }
        officeEmployeeRepository.deleteById(id);
    }
}