package com.example.logiXpert.controller;

import com.example.logiXpert.dto.CredentialsDto;
import com.example.logiXpert.dto.SignUpDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userService.login(credentialsDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto) {
        UserDto user = userService.signUp(signUpDto);
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }
}
