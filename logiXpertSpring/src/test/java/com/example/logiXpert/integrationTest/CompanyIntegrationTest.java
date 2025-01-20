package com.example.logiXpert.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.logiXpert.dto.CompanyDto;
import com.example.logiXpert.model.*;
import com.example.logiXpert.repository.*;
import com.example.logiXpert.security.JwtUtils;
import com.example.logiXpert.service.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CompanyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OfficeEmployeeRepository officeEmployeeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    private String adminToken;

    @BeforeEach
    @Rollback
    void setUp() {
        officeEmployeeRepository.deleteAll();
        clientRepository.deleteAll();
        adminRepository.deleteAll();
        officeRepository.deleteAll();
        companyRepository.deleteAll();
        roleRepository.deleteAll();

        initDatabase();
    }

    private void initDatabase() {
        if (!roleRepository.findByName(ERole.ADMIN).isPresent()) {
            Role roleAdmin = new Role();
            roleAdmin.setName(ERole.ADMIN);
            roleRepository.save(roleAdmin);
        }

        if (!roleRepository.findByName(ERole.OFFICE_EMPLOYEE).isPresent()) {
            Role roleOfficeEmployee = new Role();
            roleOfficeEmployee.setName(ERole.OFFICE_EMPLOYEE);
            roleRepository.save(roleOfficeEmployee);
        }

        Company company = new Company();
        company.setName("TestCompany");
        company.setBaseCapital(50000.0);
        companyRepository.save(company);

        Office office = new Office();
        office.setName("TestOffice");
        office.setPhone("123456789");
        office.setAddress("Sofia, bul. Bulgaria 100");
        office.setCompany(company);
        officeRepository.save(office);

        OfficeEmployee adminUser = new OfficeEmployee();
        adminUser.setName("AdminUser");
        adminUser.setEmail("admin@test.com");
        adminUser.setPhone("0000000000");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setOffice(office);
        adminUser.setCompany(company);
        adminUser.setSalary(1500.0);
        adminUser.setRoles(Collections.singleton(roleRepository.findByName(ERole.ADMIN).get()));
        officeEmployeeRepository.save(adminUser);

        OfficeEmployee officeUser = new OfficeEmployee();
        officeUser.setName("OfficeUser");
        officeUser.setEmail("office@test.com");
        officeUser.setPhone("1111111111");
        officeUser.setPassword(passwordEncoder.encode("office123"));
        officeUser.setOffice(office);
        officeUser.setCompany(company);
        officeUser.setSalary(1000.0);
        officeUser.setRoles(Collections.singleton(roleRepository.findByName(ERole.OFFICE_EMPLOYEE).get()));
        officeEmployeeRepository.save(officeUser);

        adminToken = jwtUtils.generateJwtToken(UserDetailsImpl.build(adminUser));
    }

    @Test
    @DisplayName("POST /company/add - Add a company with valid ADMIN token")
    void testAddCompanyWithAdminToken() throws Exception {
        CompanyDto companyDto = new CompanyDto(null, "NewCompany", 20000.0);

        mockMvc.perform(post("/company/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(objectMapper.writeValueAsString(companyDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewCompany"))
                .andExpect(jsonPath("$.baseCapital").value(20000.0));
    }

    @Test
    @DisplayName("GET /company/find/{id} - Get a company by ID with valid ADMIN token")
    void testGetCompanyByIdWithAdminToken() throws Exception {
        Company company = new Company();
        company.setName("FindCompany");
        company.setBaseCapital(30000.0);
        company = companyRepository.save(company);

        mockMvc.perform(get("/company/find/{id}", company.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FindCompany"))
                .andExpect(jsonPath("$.baseCapital").value(30000.0));
    }

    @Test
    @DisplayName("PUT /company/update - Update a company with valid ADMIN token")
    void testUpdateCompanyWithAdminToken() throws Exception {
        Company company = new Company();
        company.setName("OldCompany");
        company.setBaseCapital(40000.0);
        company = companyRepository.save(company);

        CompanyDto updatedCompanyDto = new CompanyDto(company.getId(), "UpdatedCompany", 50000.0);

        mockMvc.perform(put("/company/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(objectMapper.writeValueAsString(updatedCompanyDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("UpdatedCompany"))
                .andExpect(jsonPath("$.baseCapital").value(50000.0));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("DELETE /company/delete/{id} - Delete a company with valid ADMIN token")
    void testDeleteCompanyWithAdminToken() throws Exception {
        Company company = new Company();
        company.setName("ToDeleteCompany");
        company.setBaseCapital(10000.0);
        company = companyRepository.save(company);

        mockMvc.perform(delete("/company/delete/{id}", company.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/company/find/{id}", company.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
