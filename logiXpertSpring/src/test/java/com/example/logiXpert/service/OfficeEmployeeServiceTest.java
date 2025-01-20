package com.example.logiXpert.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.RegisterOfficeEmployeeDto;
import com.example.logiXpert.mapper.OfficeEmployeeMapper;
import com.example.logiXpert.model.ERole;
import com.example.logiXpert.model.Office;
import com.example.logiXpert.model.OfficeEmployee;
import com.example.logiXpert.model.Role;
import com.example.logiXpert.repository.OfficeEmployeeRepository;
import com.example.logiXpert.repository.OfficeRepository;
import com.example.logiXpert.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

class OfficeEmployeeServiceTest {

    @Mock
    private OfficeEmployeeRepository officeEmployeeRepository;

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private OfficeEmployeeMapper officeEmployeeMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private OfficeEmployeeServiceImpl officeEmployeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOfficeEmployee_Success() {
        RegisterOfficeEmployeeDto registrationDto = new RegisterOfficeEmployeeDto(
                0, "John Doe", "0888123456", "john.doe@example.com", 2500.0, "Central Office", "password123"
        );

        Office office = new Office();
        office.setName("Central Office");

        Role role = new Role();
        role.setName(ERole.OFFICE_EMPLOYEE);

        OfficeEmployee employee = new OfficeEmployee();
        employee.setName(registrationDto.name());
        employee.setEmail(registrationDto.email());
        employee.setPhone(registrationDto.phone());
        employee.setSalary(registrationDto.salary());

        when(officeRepository.findByName("Central Office")).thenReturn(Optional.of(office));
        when(roleRepository.findByName(ERole.OFFICE_EMPLOYEE)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(officeEmployeeMapper.toEntity(registrationDto)).thenReturn(employee);
        when(officeEmployeeRepository.save(any(OfficeEmployee.class))).thenReturn(employee);
        when(officeEmployeeMapper.toDto(employee)).thenReturn(new OfficeEmployeeDto(0, "John Doe", "0888123456", "john.doe@example.com", 2500.0, "Central Office", null, "OFFICE_EMPLOYEE"));

        OfficeEmployeeDto result = officeEmployeeService.addOfficeEmployee(registrationDto);

        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals("Central Office", result.officeName());
        verify(officeRepository).findByName("Central Office");
        verify(roleRepository).findByName(ERole.OFFICE_EMPLOYEE);
        verify(passwordEncoder).encode("password123");
        verify(officeEmployeeRepository).save(any(OfficeEmployee.class));
    }

    @Test
    void testGetOfficeEmployeeById_NotFound() {
        int employeeId = 1;
        when(officeEmployeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> officeEmployeeService.getOfficeEmployeeById(employeeId));
        assertEquals("OfficeEmployee with id 1 was not found", exception.getMessage());
    }

    @Test
    void testUpdateOfficeEmployee_Success() {
        OfficeEmployeeDto dto = new OfficeEmployeeDto(1, "Updated Name", "0888777666", "updated@example.com", 3000.0, "Updated Office", null, "OFFICE_EMPLOYEE");

        OfficeEmployee existingEmployee = new OfficeEmployee();
        existingEmployee.setName("Old Name");
        existingEmployee.setEmail("old@example.com");

        when(officeEmployeeRepository.findById(dto.id())).thenReturn(Optional.of(existingEmployee));
        when(officeEmployeeMapper.toEntity(dto)).thenReturn(existingEmployee);
        when(officeEmployeeRepository.save(existingEmployee)).thenReturn(existingEmployee);
        when(officeEmployeeMapper.toDto(existingEmployee)).thenReturn(dto);

        OfficeEmployeeDto result = officeEmployeeService.updateOfficeEmployee(dto);

        assertNotNull(result);
        assertEquals("Updated Name", result.name());
        assertEquals("0888777666", result.phone());
    }
}

