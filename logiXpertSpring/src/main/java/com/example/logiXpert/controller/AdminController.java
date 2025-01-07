package com.example.logiXpert.controller;

import com.example.logiXpert.dto.*;
import com.example.logiXpert.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/debug-roles")
    public ResponseEntity<?> debugRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No authentication or authorities");
        }
        return ResponseEntity.ok(authentication.getAuthorities());
    }
}
