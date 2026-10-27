package com.microservice.auth_service.controller;

import com.microservice.auth_service.dto.AuthRequest;
import com.microservice.auth_service.dto.AuthResponse;
import com.microservice.auth_service.dto.RegisterRequest;
import com.microservice.auth_service.entity.User;
import com.microservice.auth_service.service.AuthService;
import com.microservice.auth_service.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest   userRequest) {
        Optional<User> user = authService.createdUser(userRequest);
        log.info("User created Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(user.get());

    }


    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody AuthRequest request) {
        System.out.println("Login API Called");
        User user = authService.validateUser(request);

        String token =
                jwtUtil.generateToken(
                        user.getUsername());
        System.out.println("Generated Token = " + token);
        return new AuthResponse(token);
    }
}
