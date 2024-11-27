package com.example.logiXpert.controller;

import com.example.logiXpert.dto.CredentialsDto;
import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class AuthController {

    private final UserService userService;

    AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody CredentialsDto credentials) {
        UserDto user = userService.login(credentials);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        UserDto user = userService.signUp(signUpDto);
        return ResponseEntity.ok(user);
    }
}
