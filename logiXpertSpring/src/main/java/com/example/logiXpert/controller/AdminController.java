package com.example.logiXpert.controller;

import com.example.logiXpert.dto.AdminDto;
import com.example.logiXpert.dto.GetAdminDto;
import com.example.logiXpert.model.Admin;
import com.example.logiXpert.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    AdminController(AdminService adminService) {
        this.adminService = adminService;
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
}
