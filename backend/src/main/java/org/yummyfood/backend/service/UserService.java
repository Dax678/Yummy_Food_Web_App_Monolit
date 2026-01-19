package org.yummyfood.backend.service;

import org.yummyfood.backend.domain.User;
import org.yummyfood.backend.dto.request.LoginRequest;
import org.yummyfood.backend.dto.request.RegisterRequest;

public interface UserService {
    User register(RegisterRequest request);
    User authenticate(LoginRequest request);
}
