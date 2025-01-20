package com.example.logiXpert.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.logiXpert.dto.OfficeDto;
import com.example.logiXpert.exception.CompanyNotFoundException;
import com.example.logiXpert.exception.OfficeNotFoundException;
import com.example.logiXpert.mapper.OfficeMapper;
import com.example.logiXpert.model.Company;
import com.example.logiXpert.model.Office;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.repository.OfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class OfficeServiceTest {
    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private OfficeMapper officeMapper;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private OfficeServiceImpl officeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOffice_Success() {
        OfficeDto officeDto = new OfficeDto(1, "Office1", "123456", "Address1", "Company1");
        Office office = new Office("Address1", "Office1", "123456");
        Company company = new Company();
        company.setName("Company1");
        office.setCompany(company);

        when(officeMapper.toEntity(officeDto)).thenReturn(office);
        when(companyRepository.findByName("Company1")).thenReturn(Optional.of(company));
        when(officeRepository.save(office)).thenReturn(office);
        when(officeMapper.toDto(office)).thenReturn(officeDto);

        OfficeDto result = officeService.addOffice(officeDto);

        assertNotNull(result);
        assertEquals("Office1", result.name());
        verify(officeRepository).save(office);
    }

    @Test
    void testAddOffice_CompanyNotFound() {
        OfficeDto officeDto = new OfficeDto(1, "Office1", "123456", "Address1", "Company1");

        when(companyRepository.findByName("Company1")).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> officeService.addOffice(officeDto));
        verify(officeRepository, never()).save(any());
    }

    @Test
    void testUpdateOffice_Success() {
        OfficeDto officeDto = new OfficeDto(1, "UpdatedOffice", "654321", "UpdatedAddress", "UpdatedCompany");
        Office existingOffice = new Office("Address1", "Office1", "123456");
        Company updatedCompany = new Company();
        updatedCompany.setName("UpdatedCompany");

        when(officeRepository.findById(1)).thenReturn(Optional.of(existingOffice));
        when(companyRepository.findByName("UpdatedCompany")).thenReturn(Optional.of(updatedCompany));
        when(officeRepository.save(existingOffice)).thenReturn(existingOffice);
        when(officeMapper.toDto(existingOffice)).thenReturn(officeDto);

        OfficeDto result = officeService.updateOffice(officeDto);

        assertNotNull(result);
        assertEquals("UpdatedOffice", result.name());
        verify(officeRepository).save(existingOffice);
    }

    @Test
    void testUpdateOffice_NotFound() {
        OfficeDto officeDto = new OfficeDto(1, "Office1", "123456", "Address1", "Company1");

        when(officeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OfficeNotFoundException.class, () -> officeService.updateOffice(officeDto));
        verify(officeRepository, never()).save(any());
    }

    @Test
    void testGetOfficeById_Success() {
        Office office = new Office("Address1", "Office1", "123456");
        OfficeDto officeDto = new OfficeDto(1, "Office1", "123456", "Address1", "Company1");

        when(officeRepository.findById(1)).thenReturn(Optional.of(office));
        when(officeMapper.toDto(office)).thenReturn(officeDto);

        OfficeDto result = officeService.getOfficeById(1);

        assertNotNull(result);
        assertEquals("Office1", result.name());
    }

    @Test
    void testGetOfficeById_NotFound() {
        when(officeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OfficeNotFoundException.class, () -> officeService.getOfficeById(1));
    }

    @Test
    void testGetAllOffices() {
        Office office1 = new Office("Address1", "Office1", "123456");
        Office office2 = new Office("Address2", "Office2", "654321");
        OfficeDto officeDto1 = new OfficeDto(1, "Office1", "123456", "Address1", "Company1");
        OfficeDto officeDto2 = new OfficeDto(2, "Office2", "654321", "Address2", "Company2");

        when(officeRepository.findAll()).thenReturn(Stream.of(office1, office2).collect(Collectors.toList()));
        when(officeMapper.toDto(office1)).thenReturn(officeDto1);
        when(officeMapper.toDto(office2)).thenReturn(officeDto2);

        List<OfficeDto> result = officeService.getAllOffices();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteOffice_Success() {
        when(officeRepository.existsById(1)).thenReturn(true);

        officeService.deleteOffice(1);

        verify(officeRepository).deleteOfficeById(1);
    }

    @Test
    void testDeleteOffice_NotFound() {
        when(officeRepository.existsById(1)).thenReturn(false);

        assertThrows(OfficeNotFoundException.class, () -> officeService.deleteOffice(1));
        verify(officeRepository, never()).deleteOfficeById(1);
    }
}
