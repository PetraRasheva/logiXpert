package com.example.logiXpert.service;

import com.example.logiXpert.model.OfficeEmployee;

public interface OfficeEmployeeService {
    OfficeEmployee addOfficeEmployee(OfficeEmployee officeEmployee);

    OfficeEmployee updateOfficeEmployee(OfficeEmployee officeEmployee);

    OfficeEmployee getOfficeEmployeeById(Integer id);

    void deleteOfficeEmployee(Integer id);
}
