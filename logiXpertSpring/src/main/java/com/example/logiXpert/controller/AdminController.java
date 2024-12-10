package com.example.logiXpert.controller;

import com.example.logiXpert.dto.*;
import com.example.logiXpert.service.AdminService;
import com.example.logiXpert.service.CourierService;
import com.example.logiXpert.service.OfficeEmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
//@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final OfficeEmployeeService officeEmployeeService;

    private final CourierService courierService;

    AdminController(AdminService adminService, OfficeEmployeeService officeEmployeeService, CourierService courierService) {
        this.adminService = adminService;
        this.officeEmployeeService = officeEmployeeService;
        this.courierService = courierService;

    }


    @GetMapping("/find/{id}")
    public ResponseEntity<GetAdminDto> getAdminById(@PathVariable("id") Integer id) {
        GetAdminDto admin = adminService.getAdminById(id);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<AdminDto> addAdmin(@RequestBody AdminDto admin) {
        AdminDto newAdmin = adminService.addAdmin(admin);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<GetAdminDto> updateAdmin(@RequestBody AdminDto admin) {
        GetAdminDto updateAdmin = adminService.updateAdmin(admin);
        return new ResponseEntity<>(updateAdmin, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") Integer id) {
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add-employee")
    public ResponseEntity<OfficeEmployeeDto> addOfficeEmployee(@RequestBody RegisterOfficeEmployeeDto registrationDto) {
        OfficeEmployeeDto newOfficeEmployee = officeEmployeeService.addOfficeEmployee(registrationDto);
        return new ResponseEntity<>(newOfficeEmployee, HttpStatus.CREATED);
    }

    @PostMapping("/add-courier")
    public ResponseEntity<CourierDto> addCourier(@RequestBody RegisterCourierDto registrationDto) {
        CourierDto newCourier = courierService.addCourier(registrationDto);
        return new ResponseEntity<>(newCourier, HttpStatus.CREATED);
    }

    @GetMapping("/debug-roles")
    public ResponseEntity<?> debugRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No authentication or authorities");
        }

        return ResponseEntity.ok(authentication.getAuthorities());
    }
}
