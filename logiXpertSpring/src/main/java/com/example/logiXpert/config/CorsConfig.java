package com.example.logiXpert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // Разреши всички URL пътища
//                        .allowedOrigins("http://localhost:4200") // Angular приложението
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Разрешени методи
//                        .allowedHeaders("*") // Разрешени хедъри
//                        .allowCredentials(true); // Позволи изпращане на креденциали (ако е нужно)
//            }
//        };
//    }
//}