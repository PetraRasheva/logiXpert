package com.example.logiXpert.controller;

import com.example.logiXpert.dto.OfficeEmployeeDto;
import com.example.logiXpert.dto.RegisterOfficeEmployeeDto;
import com.example.logiXpert.service.OfficeEmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfficeEmployeeController.class)
class OfficeEmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfficeEmployeeService officeEmployeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterOfficeEmployeeDto registrationDto;
    private OfficeEmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        registrationDto = new RegisterOfficeEmployeeDto(0, "John Doe", "0888123456", "john.doe@example.com", 2500.0, "Central Office", "password123");
        employeeDto = new OfficeEmployeeDto(1, "John Doe", "0888123456", "john.doe@example.com", 2500.0, "Central Office", "MyCompany", "OFFICE_EMPLOYEE");
    }

    @Test
    void testAddOfficeEmployee() throws Exception {
        Mockito.when(officeEmployeeService.addOfficeEmployee(Mockito.any())).thenReturn(employeeDto);

        mockMvc.perform(post("/employee/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.officeName").value("Central Office"));
    }

    @Test
    void testGetOfficeEmployeeById() throws Exception {
        Mockito.when(officeEmployeeService.getOfficeEmployeeById(1)).thenReturn(employeeDto);

        mockMvc.perform(get("/employee/find/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.officeName").value("Central Office"));
    }

    @Test
    void testUpdateOfficeEmployee() throws Exception {
        Mockito.when(officeEmployeeService.updateOfficeEmployee(Mockito.any())).thenReturn(employeeDto);

        mockMvc.perform(put("/employee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.officeName").value("Central Office"));
    }

    @Test
    void testDeleteOfficeEmployee() throws Exception {
        Mockito.doNothing().when(officeEmployeeService).deleteOfficeEmployee(1);

        mockMvc.perform(delete("/employee/admin/delete/1"))
                .andExpect(status().isNoContent());
    }
}