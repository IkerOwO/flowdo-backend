package com.iker.flowdo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.iker.flowdo.dtos.auth.LoginRequest;
import com.iker.flowdo.dtos.auth.LoginResponse;
import com.iker.flowdo.dtos.auth.RegisterUserRequest;
import com.iker.flowdo.services.AuthService;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/auth") 
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        service.registerUser(request);
        return ResponseEntity.ok("User created!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.loginUser(request));
    }
}
