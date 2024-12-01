package com.example.logiXpert.controller;

import com.example.logiXpert.dto.CredentialsDto;
import com.example.logiXpert.dto.JwtResponse;
import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.exception.UserException;
import com.example.logiXpert.model.User;
import com.example.logiXpert.repository.UserRepository;
import com.example.logiXpert.security.JwtUtils;
import com.example.logiXpert.service.UserDetailsImpl;
import com.example.logiXpert.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody CredentialsDto credentials, HttpServletResponse response) {
        User user = userRepository.findUserByEmail(credentials.email())
                .orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(credentials.password(), user.getPassword())) {
            throw new UserException("Invalid password", HttpStatus.UNAUTHORIZED);
        }

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String jwt = jwtUtils.generateJwtToken(userDetails);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());


        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .toList();
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", user.getId());
        responseBody.put("email", user.getEmail());
        responseBody.put("name", user.getName());
        responseBody.put("phone", user.getPhone());
        responseBody.put("roles", roles);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        UserDto user = userService.signUp(signUpDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", null)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());
        return ResponseEntity.ok("Logout successful");
    }
}
