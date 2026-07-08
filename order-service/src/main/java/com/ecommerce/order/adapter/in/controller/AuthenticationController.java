package com.ecommerce.order.adapter.in.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.order.adapter.in.controller.dto.LoginRequest;
import com.ecommerce.order.adapter.in.controller.dto.LoginResponse;
import com.ecommerce.order.adapter.in.security.AuthService;
import com.ecommerce.order.adapter.in.security.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthService authService;

    public AuthenticationController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.authenticate(request.getUsername(), request.getPassword())
                .map(user -> {
                    String token = jwtService.generateToken(user.username(), user.roles());

                    LoginResponse response = new LoginResponse();
                    response.setToken(token);
                    response.setTokenType("Bearer");
                    response.setExpiresInSeconds(86400L);
                    response.setUsername(user.username());
                    response.setRoles(user.roles());

                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}