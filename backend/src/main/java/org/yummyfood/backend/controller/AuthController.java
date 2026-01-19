package org.yummyfood.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yummyfood.backend.config.security.JwtService;
import org.yummyfood.backend.domain.User;
import org.yummyfood.backend.dto.request.LoginRequest;
import org.yummyfood.backend.dto.request.RegisterRequest;
import org.yummyfood.backend.dto.response.AuthResponse;
import org.yummyfood.backend.mapper.UserMapper;
import org.yummyfood.backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        String token = jwtService.generateToken(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AuthResponse(token, userMapper.toUserResponse(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request);
        String token = jwtService.generateToken(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AuthResponse(token, userMapper.toUserResponse(user)));
    }
}
