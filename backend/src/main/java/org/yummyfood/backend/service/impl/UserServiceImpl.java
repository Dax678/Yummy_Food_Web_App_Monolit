package org.yummyfood.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yummyfood.backend.domain.User;
import org.yummyfood.backend.domain.model.UserRole;
import org.yummyfood.backend.dto.request.LoginRequest;
import org.yummyfood.backend.dto.request.RegisterRequest;
import org.yummyfood.backend.exception.InvalidInputException;
import org.yummyfood.backend.exception.NotFoundException;
import org.yummyfood.backend.repository.UserRepository;
import org.yummyfood.backend.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new InvalidInputException("Email already in use.");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new InvalidInputException("Username already in use.");
        }
        if(request.role() != UserRole.USER && request.role() != UserRole.RESTAURANT) {
            throw new InvalidInputException("Invalid role for registration.");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .phone(request.phone())
                .role(request.role())
                .build();


        return userRepository.save(user);
    }

    @Override
    public User authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("User not found for email: " + request.email()));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidInputException("Invalid credentials.");
        }

        return user;
    }
}
