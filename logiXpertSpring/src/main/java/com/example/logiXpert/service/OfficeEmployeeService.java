package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.OfficeEmployeeRegistrationDto;

public interface OfficeEmployeeService {

    OfficeEmployeeDto addOfficeEmployee(OfficeEmployeeRegistrationDto registrationDto);

    OfficeEmployeeDto updateOfficeEmployee(OfficeEmployeeDto officeEmployeeDto);

    OfficeEmployeeDto getOfficeEmployeeById(Integer id);

    void deleteOfficeEmployee(Integer id);
}