package com.example.logiXpert.controller;

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
    public ResponseEntity<Admin> getAdminById (@PathVariable("id") Integer id) {
        Admin admin = adminService.getAdminById(id);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Admin> addAdmin(@RequestBody Admin admin) {
        Admin newAdmin = adminService.addAdmin(admin);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Admin> updateAdmin(@RequestBody Admin admin) {
        Admin updateAdmin = adminService.updateAdmin(admin);
        return new ResponseEntity<>(updateAdmin, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") Integer id) {
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
