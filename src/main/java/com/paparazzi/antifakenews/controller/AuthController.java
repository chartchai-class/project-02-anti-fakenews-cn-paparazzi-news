package com.paparazzi.antifakenews.controller;

import com.paparazzi.antifakenews.dto.JwtResponse;
import com.paparazzi.antifakenews.dto.LoginRequest;
import com.paparazzi.antifakenews.dto.RegisterRequest;
import com.paparazzi.antifakenews.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
