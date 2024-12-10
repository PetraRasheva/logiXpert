package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.RegisterOfficeEmployeeDto;
import jakarta.transaction.Transactional;

public interface OfficeEmployeeService {

    OfficeEmployeeDto addOfficeEmployee(RegisterOfficeEmployeeDto registrationDto);

    OfficeEmployeeDto updateOfficeEmployee(OfficeEmployeeDto officeEmployeeDto);

    @Transactional
    OfficeEmployeeDto updateOfficeEmployeeByAdmin(OfficeEmployeeDto officeEmployeeDto);

    OfficeEmployeeDto getOfficeEmployeeById(Integer id);

    void deleteOfficeEmployee(Integer id);
}