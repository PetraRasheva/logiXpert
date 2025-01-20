package com.example.logiXpert.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.logiXpert.model.Company;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void testSaveCompany() {
        Company company = new Company("Test Company", 10000.0);
        Company savedCompany = companyRepository.save(company);

        assertNotNull(savedCompany);
        assertNotNull(savedCompany.getId());
        assertEquals("Test Company", savedCompany.getName());
        assertEquals(10000.0, savedCompany.getBaseCapital());
    }

    @Test
    void testFindById() {
        Company company = new Company("Test Company", 10000.0);
        companyRepository.save(company);

        Optional<Company> foundCompany = companyRepository.findById(company.getId());

        assertTrue(foundCompany.isPresent());
        assertEquals("Test Company", foundCompany.get().getName());
    }

    @Test
    void testFindByName() {
        Company company = new Company("Unique Company", 50000.0);
        companyRepository.save(company);

        Optional<Company> foundCompany = companyRepository.findByName("Unique Company");

        assertTrue(foundCompany.isPresent());
        assertEquals(50000.0, foundCompany.get().getBaseCapital());
    }

    @Test
    void testDeleteCompanyById() {
        Company company = new Company("Deletable Company", 20000.0);
        company = companyRepository.save(company);

        companyRepository.deleteCompanyById(company.getId());

        Optional<Company> deletedCompany = companyRepository.findById(company.getId());
        assertFalse(deletedCompany.isPresent());
    }

    @Test
    void testFindAll() {
        Company company1 = new Company("Company A", 30000.0);
        Company company2 = new Company("Company B", 40000.0);
        companyRepository.save(company1);
        companyRepository.save(company2);

        Iterable<Company> companies = companyRepository.findAll();

        assertNotNull(companies);
        assertTrue(companies.iterator().hasNext());
    }
}

