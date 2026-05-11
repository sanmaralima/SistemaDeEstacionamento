package com.locuspark.api.controller;

import com.locuspark.api.dto.request.AuthRequest;
import com.locuspark.api.dto.request.RegisterRequest;
import com.locuspark.api.dto.response.AuthResponse;
import com.locuspark.api.entity.User;
import com.locuspark.api.enums.UserRole;
import com.locuspark.api.repository.UserRepository;
import com.locuspark.api.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder; 

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest data) {
        if (repository.findByUsername(data.username()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String encryptedPassword = passwordEncoder.encode(data.password());

        User newUser = new User(data.username(), encryptedPassword, UserRole.USER);

        repository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}