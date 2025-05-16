package com.bohemio.todoreactspringboot.service;

import com.bohemio.todoreactspringboot.dto.JwtResponse;
import com.bohemio.todoreactspringboot.dto.LoginRequest;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
}
