package com.example.logiXpert.controller;

import com.example.logiXpert.dto.GetUserDto;
import com.example.logiXpert.dto.UserDto;
import com.example.logiXpert.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@PreAuthorize("!hasAuthority('COURIER')")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<GetUserDto> getUserById (@PathVariable("id") Integer id) {
        GetUserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto user) {
        UserDto newUser = userService.addUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user) {
        UserDto updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}