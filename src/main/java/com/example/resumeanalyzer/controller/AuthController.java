package com.example.resumeanalyzer.controller;

import com.example.resumeanalyzer.model.UserEntity;
import com.example.resumeanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // ✅ allow frontend (important)
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ SIGNUP (JSON)
    @PostMapping("/signup")
    public String signup(@RequestBody UserEntity user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "User already exists";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "Signup successful";
    }

    // ✅ LOGIN (JSON)
    @PostMapping("/login")
    public String login(@RequestBody UserEntity user) {

        return userRepository.findByUsername(user.getUsername())
                .filter(u -> passwordEncoder.matches(user.getPassword(), u.getPassword()))
                .map(u -> "Login success")
                .orElse("Invalid credentials");
    }
}