package com.paparazzi.antifakenews.service;

import com.paparazzi.antifakenews.dto.JwtResponse;
import com.paparazzi.antifakenews.dto.LoginRequest;
import com.paparazzi.antifakenews.dto.RegisterRequest;
import com.paparazzi.antifakenews.model.User;
import com.paparazzi.antifakenews.model.enums.Role;
import com.paparazzi.antifakenews.repository.UserRepository;
import com.paparazzi.antifakenews.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) throw new IllegalArgumentException("email exists");
        User u = new User();
        u.setName(req.getName());
        u.setSurname(req.getSurname());
        u.setEmail(req.getEmail());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        u.setProfileImageUrl(req.getProfileImageUrl());
        u.setRole(Role.USER);
        userRepository.save(u);
    }

    public JwtResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        String token = jwtUtil.generateToken(auth.getName());
        return new JwtResponse(token);
    }
}
