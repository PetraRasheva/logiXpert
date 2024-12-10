package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.RegisterOfficeEmployeeDto;
import com.example.logiXpert.exception.OfficeEmployeeNotFoundException;
import com.example.logiXpert.mapper.OfficeEmployeeMapper;
import com.example.logiXpert.model.ERole;
import com.example.logiXpert.model.Office;
import com.example.logiXpert.model.OfficeEmployee;
import com.example.logiXpert.model.Role;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.OfficeEmployeeRepository;
import com.example.logiXpert.repository.OfficeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.logiXpert.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class OfficeEmployeeServiceImpl implements OfficeEmployeeService {

    private final OfficeEmployeeRepository officeEmployeeRepository;
    private final OfficeRepository officeRepository;
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
        this.officeEmployeeMapper = officeEmployeeMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OfficeEmployeeDto addOfficeEmployee(RegisterOfficeEmployeeDto registrationDto) {

        OfficeEmployee employee = officeEmployeeMapper.toEntity(registrationDto);
        employee.setPassword(passwordEncoder.encode(registrationDto.password()));

        employee.setOffice(
                officeRepository.findByName(registrationDto.officeName())
                        .orElseThrow(() -> new OfficeEmployeeNotFoundException("Office not found"))
        );

        employee.setCompany(
                employee.getOffice().getCompany()
        );


        Role role = roleRepository.findByName(ERole.OFFICE_EMPLOYEE)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        employee.getRoles().add(role);
        OfficeEmployee savedEmployee = officeEmployeeRepository.save(employee);
        return officeEmployeeMapper.toDto(savedEmployee);
    }

    @Override
    public OfficeEmployeeDto updateOfficeEmployee(OfficeEmployeeDto officeEmployeeDto) {
        OfficeEmployee existingEmployee = officeEmployeeRepository.findById(officeEmployeeDto.id())
                .orElseThrow(() -> new OfficeEmployeeNotFoundException("OfficeEmployee not found"));

        OfficeEmployee updatedEmployee = officeEmployeeMapper.toEntity(officeEmployeeDto);

        updatedEmployee.setPassword(existingEmployee.getPassword());
        updatedEmployee.setSalary(existingEmployee.getSalary());
        updatedEmployee.setRoles(existingEmployee.getRoles());
        updatedEmployee.setOffice(existingEmployee.getOffice());
        updatedEmployee.setCompany(existingEmployee.getCompany());

        OfficeEmployee savedEmployee = officeEmployeeRepository.save(updatedEmployee);

        return officeEmployeeMapper.toDto(savedEmployee);
    }

    @Override
    @Transactional
    public OfficeEmployeeDto updateOfficeEmployeeByAdmin(OfficeEmployeeDto officeEmployeeDto) {
        OfficeEmployee existingEmployee = officeEmployeeRepository.findById(officeEmployeeDto.id())
                .orElseThrow(() -> new OfficeEmployeeNotFoundException("OfficeEmployee not found"));

        OfficeEmployee updatedEmployee = officeEmployeeMapper.toEntity(officeEmployeeDto);

        updatedEmployee.setPassword(existingEmployee.getPassword());
        updatedEmployee.setRoles(existingEmployee.getRoles());
        updatedEmployee.setCompany(existingEmployee.getCompany());

        if (officeEmployeeDto.officeName() != null) {
            Office office = officeRepository.findByName(officeEmployeeDto.officeName())
                    .orElseThrow(() -> new RuntimeException("Office not found with name: " + officeEmployeeDto.officeName()));
            updatedEmployee.setOffice(office);
        } else {
            updatedEmployee.setOffice(existingEmployee.getOffice());
        }

        OfficeEmployee savedEmployee = officeEmployeeRepository.save(updatedEmployee);

        return officeEmployeeMapper.toDto(savedEmployee);
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