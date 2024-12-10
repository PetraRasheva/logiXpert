package com.example.logiXpert.security;

import com.example.logiXpert.service.UserDetailsServiceImpl;
import com.example.logiXpert.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public SecurityConfig(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/api/auth/").permitAll()
//                        .requestMatchers("/admin/").hasAuthority("ADMIN")
//                        .requestMatchers("/admin/hire-office-employee").hasAuthority("ADMIN")
//                        .anyRequest().authenticated()
//                )

                .csrf(csrf -> csrf.disable()) // Изключва CSRF
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/admin/**", "/company/**", "/office/**", "/employee/admin/**",
                                "/employee/find/{id}").hasAuthority("ADMIN")
                        .requestMatchers("/client/**").hasAuthority("CLIENT")
                        .requestMatchers("/employee/**").hasAuthority("OFFICE_EMPLOYEE")
                        .requestMatchers("/courier/**").hasAuthority("COURIER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(jwtUtils, userDetailsServiceImpl),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource())); // CORS конфигурация;

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsServiceImpl) {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtils);
        filter.setUserDetailsService(userDetailsServiceImpl);
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
