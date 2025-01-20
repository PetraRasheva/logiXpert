package com.example.logiXpert.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.logiXpert.dto.OfficeDto;
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
public class OfficeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OfficeEmployeeRepository officeEmployeeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdminRepository adminRepository;

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

        Company company = new Company();
        company.setName("TestCompany");
        company.setBaseCapital(50000.0);
        companyRepository.save(company);

        OfficeEmployee adminUser = new OfficeEmployee();
        adminUser.setName("AdminUser");
        adminUser.setEmail("admin@test.com");
        adminUser.setPhone("0000000000");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setCompany(company);
        adminUser.setRoles(Collections.singleton(roleRepository.findByName(ERole.ADMIN).get()));
        officeEmployeeRepository.save(adminUser);

        adminToken = jwtUtils.generateJwtToken(UserDetailsImpl.build(adminUser));
    }

    @Test
    @DisplayName("POST /office/add - Add an office with valid ADMIN token")
    void testAddOfficeWithAdminToken() throws Exception {
        OfficeDto officeDto = new OfficeDto(0, "NewOffice", "123456789", "Address1", "TestCompany");

        mockMvc.perform(post("/office/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(objectMapper.writeValueAsString(officeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewOffice"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.address").value("Address1"));
    }

    @Test
    @DisplayName("GET /office/find/{id} - Get an office by ID with valid ADMIN token")
    void testGetOfficeByIdWithAdminToken() throws Exception {
        Company company = companyRepository.findAll().get(0);
        Office office = new Office("Address1", "Office1", "123456");
        office.setCompany(company);
        office = officeRepository.save(office);

        mockMvc.perform(get("/office/find/{id}", office.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Office1"))
                .andExpect(jsonPath("$.phone").value("123456"))
                .andExpect(jsonPath("$.address").value("Address1"));
    }

    @Test
    @DisplayName("PUT /office/update - Update an office with valid ADMIN token")
    void testUpdateOfficeWithAdminToken() throws Exception {
        Company company = companyRepository.findAll().get(0);
        Office office = new Office("Address1", "Office1", "123456");
        office.setCompany(company);
        office = officeRepository.save(office);

        OfficeDto updatedOfficeDto = new OfficeDto(office.getId(), "UpdatedOffice", "654321", "UpdatedAddress", "TestCompany");

        mockMvc.perform(put("/office/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(objectMapper.writeValueAsString(updatedOfficeDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/office/find/{id}", office.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedOffice"))
                .andExpect(jsonPath("$.phone").value("654321"))
                .andExpect(jsonPath("$.address").value("UpdatedAddress"));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("DELETE /office/delete/{id} - Delete an office with valid ADMIN token")
    void testDeleteOfficeWithAdminToken() throws Exception {
        Company company = companyRepository.findAll().get(0);
        Office office = new Office("Address1", "Office1", "123456");
        office.setCompany(company);
        office = officeRepository.save(office);

        mockMvc.perform(delete("/office/delete/{id}", office.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/office/find/{id}", office.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}