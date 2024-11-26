package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.exception.OfficeEmployeeNotFoundException;
import com.example.logiXpert.mapper.OfficeEmployeeMapper;
import com.example.logiXpert.model.OfficeEmployee;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.OfficeEmployeeRepository;
import com.example.logiXpert.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficeEmployeeServiceImpl implements OfficeEmployeeService {

    private final OfficeEmployeeRepository officeEmployeeRepository;
    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;
    private final OfficeEmployeeMapper officeEmployeeMapper;
    @Autowired
    public OfficeEmployeeServiceImpl(
            OfficeEmployeeRepository officeEmployeeRepository,
            OfficeRepository officeRepository,
            CompanyRepository companyRepository, OfficeEmployeeMapper officeEmployeeMapper) {
        this.officeEmployeeRepository = officeEmployeeRepository;
        this.officeRepository = officeRepository;
        this.companyRepository = companyRepository;
        this.officeEmployeeMapper = officeEmployeeMapper;
    }

    @Override
    public OfficeEmployeeDto addOfficeEmployee(OfficeEmployeeDto officeEmployeeDto) {

        OfficeEmployee employee = officeEmployeeMapper.toEntity(officeEmployeeDto);

        employee.setOffice(
                officeRepository.findByName(officeEmployeeDto.officeName())
                        .orElseThrow(() -> new OfficeEmployeeNotFoundException("Office not found"))
        );

        employee.setCompany(
                companyRepository.findByName(officeEmployeeDto.companyName())
                        .orElseThrow(() -> new OfficeEmployeeNotFoundException("Company not found"))
        );

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