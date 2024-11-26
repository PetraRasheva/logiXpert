package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeEmployeeDto;

public interface OfficeEmployeeService {
    OfficeEmployeeDto addOfficeEmployee(OfficeEmployeeDto officeEmployeeDto);

    OfficeEmployeeDto updateOfficeEmployee(OfficeEmployeeDto officeEmployeeDto);

    OfficeEmployeeDto getOfficeEmployeeById(Integer id);

    void deleteOfficeEmployee(Integer id);
}