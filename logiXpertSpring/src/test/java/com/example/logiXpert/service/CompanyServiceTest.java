package com.example.logiXpert.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.mapper.CompanyMapper;
import com.example.logiXpert.model.Company;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

public class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCompany() {
        CompanyDto companyDto = new CompanyDto(1, "Test Company", 10000.0, 5);
        Company company = new Company("Test Company", 10000.0, 5);

        when(companyMapper.toEntity(companyDto)).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.addCompany(companyDto);

        assertNotNull(result);
        assertEquals("Test Company", result.name());
        verify(companyRepository, times(1)).save(company);
    }

    @Test
    void testUpdateCompany() {
        CompanyDto companyDto = new CompanyDto(1, "Updated Company", 20000.0, 50.0);
        Company company = new Company("Test Company", 10000.0, 50.0);

        when(companyRepository.findById(1)).thenReturn(Optional.of(company));
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.updateCompany(companyDto);

        assertNotNull(result);
        assertEquals("Updated Company", result.name());
        assertEquals(20000.0, result.baseCapital());
        verify(companyRepository, times(1)).save(company);
    }

    @Test
    void testGetCompanyById() {
        Company company = new Company("Test Company", 10000.0, 50.0);
        CompanyDto companyDto = new CompanyDto(1, "Test Company", 10000.0, 50.0);

        when(companyRepository.findById(1)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.getCompanyById(1);

        assertNotNull(result);
        assertEquals("Test Company", result.name());
        verify(companyRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteCompany() {
        doNothing().when(companyRepository).deleteCompanyById(1);

        companyService.deleteCompany(1);

        verify(companyRepository, times(1)).deleteCompanyById(1);
    }

    @Test
    void testCalculateRevenue() {
        when(companyRepository.findById(1)).thenReturn(Optional.of(new Company()));
        when(shipmentRepository.calculateRevenueByCompanyId(1)).thenReturn(5000.0);

        double revenue = companyService.calculateRevenue(1);

        assertEquals(5000.0, revenue);
        verify(shipmentRepository, times(1)).calculateRevenueByCompanyId(1);
    }

    @Test
    void testCalculateRevenueDateRange() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        when(companyRepository.findById(1)).thenReturn(Optional.of(new Company()));
        when(shipmentRepository.calculateRevenueByCompanyAndDateRange(1, startDate, endDate)).thenReturn(10000.0);

        double revenue = companyService.calculateRevenueDateRange(1, startDate, endDate);

        assertEquals(10000.0, revenue);
        verify(shipmentRepository, times(1)).calculateRevenueByCompanyAndDateRange(1, startDate, endDate);
    }
}
