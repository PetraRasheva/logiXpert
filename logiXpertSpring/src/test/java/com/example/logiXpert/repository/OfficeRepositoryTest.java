package com.example.logiXpert.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.logiXpert.model.Company;
import com.example.logiXpert.model.Office;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class OfficeRepositoryTest {

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private Office office;

    @BeforeEach
    void setUp() {
        officeRepository.deleteAll();
        companyRepository.deleteAll();

        Company company = new Company("Test Company", 10000.0);
        companyRepository.save(company);

        office = new Office("Address1", "Office1", "123456");
        office.setCompany(company);
        officeRepository.save(office);
    }

    @Test
    void testSaveOffice() {
        Office newOffice = new Office("Address2", "Office2", "654321");
        Office savedOffice = officeRepository.save(newOffice);

        assertNotNull(savedOffice);
        assertNotNull(savedOffice.getId());
        assertEquals("Office2", savedOffice.getName());
    }

    @Test
    void testFindOfficeById() {
        Optional<Office> foundOffice = officeRepository.findById(office.getId());

        assertTrue(foundOffice.isPresent());
        assertEquals("Office1", foundOffice.get().getName());
    }

    @Test
    void testFindByName() {
        Company company = new Company("Test Company", 10000.0);
        company = companyRepository.save(company);

        Office office = new Office("Address1", "Test Office", "123456");
        office.setCompany(company);
        officeRepository.save(office);

        Optional<Office> foundOffice = officeRepository.findByName("Test Office");

        assertTrue(foundOffice.isPresent());
        assertEquals("Test Office", foundOffice.get().getName());
        assertEquals("Test Company", foundOffice.get().getCompany().getName());
    }

    @Test
    void testDeleteOfficeById() {
        Company company = new Company("Test Company", 10000.0);
        company = companyRepository.save(company);

        Office office = new Office("Address1", "Office1", "123456");
        office.setCompany(company);
        office = officeRepository.save(office);

        officeRepository.deleteOfficeById(office.getId());

        Optional<Office> deletedOffice = officeRepository.findById(office.getId());
        assertFalse(deletedOffice.isPresent());
    }

    @Test
    void testExistsById() {
        Company company = new Company("Test Company", 10000.0);
        company = companyRepository.save(company);

        Office office = new Office("Address1", "Office1", "123456");
        office.setCompany(company);
        office = officeRepository.save(office);

        boolean exists = officeRepository.existsById(office.getId());
        assertTrue(exists);
    }
}
