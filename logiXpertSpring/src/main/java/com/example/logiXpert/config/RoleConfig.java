package com.example.logiXpert.config;

import com.example.logiXpert.model.ERole;
import com.example.logiXpert.model.Role;
import com.example.logiXpert.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class RoleConfig {

    @Order(1)
    @Bean
    CommandLineRunner initializeRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName(ERole.ADMIN).isEmpty()) {
                roleRepository.save(new Role(ERole.CLIENT));
                roleRepository.save(new Role(ERole.ADMIN));
                roleRepository.save(new Role(ERole.OFFICE_EMPLOYEE));
                roleRepository.save(new Role(ERole.COURIER));
            }
        };
    }
}