package com.example.logiXpert.integrationTest;
import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.RegisterOfficeEmployeeDto;
import com.example.logiXpert.model.*;
import com.example.logiXpert.repository.*;
import com.example.logiXpert.security.JwtUtils;
import com.example.logiXpert.service.UserDetailsImpl;
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

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OfficeEmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfficeEmployeeRepository officeEmployeeRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String officeEmployeeToken;

    /**
     * Преди всеки тест изчиствам таблиците
     */
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
        Role roleAdmin = new Role();
        roleAdmin.setName(ERole.ADMIN);
        roleRepository.save(roleAdmin);

        Role roleOfficeEmployee = new Role();
        roleOfficeEmployee.setName(ERole.OFFICE_EMPLOYEE);
        roleRepository.save(roleOfficeEmployee);

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
        adminUser.setRoles(Collections.singleton(roleAdmin));
        officeEmployeeRepository.save(adminUser);

        OfficeEmployee officeUser = new OfficeEmployee();
        officeUser.setName("OfficeUser");
        officeUser.setEmail("office@test.com");
        officeUser.setPhone("1111111111");
        officeUser.setPassword(passwordEncoder.encode("office123"));
        officeUser.setOffice(office);
        officeUser.setCompany(company);
        officeUser.setSalary(1000.0);
        officeUser.setRoles(Collections.singleton(roleOfficeEmployee));
        officeEmployeeRepository.save(officeUser);

        adminToken = jwtUtils.generateJwtToken(UserDetailsImpl.build(adminUser));
        officeEmployeeToken = jwtUtils.generateJwtToken(UserDetailsImpl.build(officeUser));
    }

    @Test
    @DisplayName("GET /employee/find/{id} - да върне OfficeEmployeeDto с валиден ADMIN токен")
    void testGetOfficeEmployeeById_withAdminToken() throws Exception {
        Optional<OfficeEmployee> admin = officeEmployeeRepository.findByEmail("admin@test.com");
        int adminId = admin.map(OfficeEmployee::getId).orElseThrow();

        mockMvc.perform(
                        get("/employee/find/{id}", adminId)
                                .header("Authorization", "Bearer " + adminToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(adminId)))
                .andExpect(jsonPath("$.name", is("AdminUser")))
                .andExpect(jsonPath("$.email", is("admin@test.com")))
                .andExpect(jsonPath("$.role", is("ADMIN")));
    }

    @Test
    @DisplayName("POST /employee/add - да създаде нов OfficeEmployee с валиден ADMIN токен")
    void testAddOfficeEmployee_withAdminToken() throws Exception {
        RegisterOfficeEmployeeDto dto = new RegisterOfficeEmployeeDto(
                0,
                "NewOfficeEmployee",
                "2222222222",
                "newemployee@test.com",
                1500.0,
                "TestOffice",
                "office123"
        );

        mockMvc.perform(
                        post("/employee/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + adminToken)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is("NewOfficeEmployee")))
                .andExpect(jsonPath("$.phone", is("2222222222")))
                .andExpect(jsonPath("$.email", is("newemployee@test.com")))
                .andExpect(jsonPath("$.officeName", is("TestOffice")))
                .andExpect(jsonPath("$.role", is("OFFICE_EMPLOYEE")));
    }

    @Test
    @DisplayName("PUT /employee/admin/update - да актуализира само заплатата и офиса с валиден ADMIN токен")
    void testAdminUpdateOfficeEmployee_withAdminToken() throws Exception {
        Optional<OfficeEmployee> officeUser = officeEmployeeRepository.findByEmail("office@test.com");
        int officeUserId = officeUser.map(OfficeEmployee::getId).orElseThrow();

        OfficeEmployeeDto dto = new OfficeEmployeeDto(
                officeUserId,
                "OfficeUser",
                "1111111111",
                "office@test.com",
                2000.0,
                "TestOffice",
                "TestCompany",
                "OFFICE_EMPLOYEE"
        );

        mockMvc.perform(
                        put("/employee/admin/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + adminToken)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(officeUserId)))
                .andExpect(jsonPath("$.salary", is(2000.0)))
                .andExpect(jsonPath("$.officeName", is("TestOffice")));
    }

    @Test
    @DisplayName("PUT /employee/update - да актуализира само личните данни с валиден OFFICE_EMPLOYEE токен")
    void testOfficeEmployeeUpdate_withEmployeeToken() throws Exception {
        Optional<OfficeEmployee> officeUser = officeEmployeeRepository.findByEmail("office@test.com");
        int officeUserId = officeUser.map(OfficeEmployee::getId).orElseThrow();

        OfficeEmployeeDto dto = new OfficeEmployeeDto(
                officeUserId,
                "UpdatedName",
                "3333333333",
                "updated@test.com",
                1000.0,
                "TestOffice",
                "TestCompany",
                "OFFICE_EMPLOYEE"
        );

        mockMvc.perform(
                        put("/employee/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + officeEmployeeToken)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(officeUserId)))
                .andExpect(jsonPath("$.name", is("UpdatedName")))
                .andExpect(jsonPath("$.phone", is("3333333333")))
                .andExpect(jsonPath("$.email", is("updated@test.com")));
    }

    @Test
    @DisplayName("DELETE /employee/admin/delete/{id} - да изтрие OfficeEmployee с валиден ADMIN токен")
    void testDeleteOfficeEmployee_withAdminToken() throws Exception {
        Optional<OfficeEmployee> officeUser = officeEmployeeRepository.findByEmail("office@test.com");
        int officeUserId = officeUser.map(OfficeEmployee::getId).orElseThrow();

        mockMvc.perform(
                        delete("/employee/admin/delete/{id}", officeUserId)
                                .header("Authorization", "Bearer " + adminToken)
                )
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employee/find/{id}", officeUserId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

}