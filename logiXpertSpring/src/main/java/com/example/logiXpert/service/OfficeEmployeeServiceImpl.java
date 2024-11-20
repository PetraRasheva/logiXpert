package com.example.logiXpert.service;

import com.example.logiXpert.exception.OfficeEmployeeNotFoundException;
import com.example.logiXpert.model.OfficeEmployee;
import com.example.logiXpert.repository.OfficeEmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class OfficeEmployeeServiceImpl implements OfficeEmployeeService {
    
    private final OfficeEmployeeRepository officeEmployeeRepository;
    
    public OfficeEmployeeServiceImpl(OfficeEmployeeRepository officeEmployeeRepository) {
        this.officeEmployeeRepository = officeEmployeeRepository;
    }

    @Override
    public OfficeEmployee addOfficeEmployee(OfficeEmployee officeEmployee) {
        return officeEmployeeRepository.save(officeEmployee);
    }

    @Override
    public OfficeEmployee updateOfficeEmployee(OfficeEmployee officeEmployee) {
        return officeEmployeeRepository.save(officeEmployee);
    }

    @Override
    public OfficeEmployee getOfficeEmployeeById(Integer id) {
        return officeEmployeeRepository.findOfficeEmployeeById(id).orElseThrow(() -> new OfficeEmployeeNotFoundException("OfficeEmployee with id " + id + " was not found"));
    }

    @Override
    public void deleteOfficeEmployee(Integer id) {
        officeEmployeeRepository.deleteOfficeEmployeeById(id);
    }

    //TODO: Implement complex requests
}
