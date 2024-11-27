package com.example.logiXpert.config;

import com.example.logiXpert.dto.AdminDto;
import com.example.logiXpert.mapper.AdminMapper;
import com.example.logiXpert.model.Admin;
import com.example.logiXpert.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminConfig {

    @Bean
    CommandLineRunner commandLineRunner(AdminRepository adminRepository, AdminMapper adminMapper) {
        return args -> {
            if (adminRepository.findByEmail("peter@ibm.com") == null) {
                AdminDto adminDto = new AdminDto(
                        1,
                        "Peter Ivanov",
                        "peter@ibm.com",
                        "1234567890",
                        "Ndssd_32534"
                        
                );
                Admin admin = adminMapper.toEntity(adminDto);
                adminRepository.save(admin);
            }
        };
    }
}