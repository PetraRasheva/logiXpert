package com.example.logiXpert.config;

import com.example.logiXpert.dto.AdminDto;
import com.example.logiXpert.mapper.AdminMapper;
import com.example.logiXpert.model.Admin;
import com.example.logiXpert.model.ERole;
import com.example.logiXpert.model.Role;
import com.example.logiXpert.repository.AdminRepository;
import com.example.logiXpert.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class AdminConfig {

    @Order(2)
    @Bean
    CommandLineRunner commandLineRunner(
            AdminRepository adminRepository,
            RoleRepository roleRepository,
            AdminMapper adminMapper,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (adminRepository.findByEmail("peter@ibm.com") == null) {
                Optional<Role> adminRoleOptional = roleRepository.findByName(ERole.ADMIN);

                if (adminRoleOptional.isEmpty()) {
                    throw new RuntimeException("Error: Role ADMIN not found in the database.");
                }

                Role adminRole = adminRoleOptional.get();

                AdminDto adminDto = new AdminDto(
                        1,
                        "Peter Ivanov",
                        "peter@ibm.com",
                        "1234567890",
                        passwordEncoder.encode("Ndssd_32534")
                );

                Admin admin = adminMapper.toEntity(adminDto);
                admin.getRoles().add(adminRole);
                adminRepository.save(admin);
            }
        };
    }
}