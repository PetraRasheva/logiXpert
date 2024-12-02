package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.RegisterOfficeEmployeeDto;

public interface OfficeEmployeeService {

    OfficeEmployeeDto addOfficeEmployee(RegisterOfficeEmployeeDto registrationDto);

    OfficeEmployeeDto updateOfficeEmployee(OfficeEmployeeDto officeEmployeeDto);

    OfficeEmployeeDto getOfficeEmployeeById(Integer id);

    void deleteOfficeEmployee(Integer id);
}