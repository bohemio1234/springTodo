package com.bohemio.todoreactspringboot.service;

import com.bohemio.todoreactspringboot.dto.JwtResponse;
import com.bohemio.todoreactspringboot.dto.LoginRequest;
import com.bohemio.todoreactspringboot.dto.UserSignUpRequest;
import org.springframework.stereotype.Service;


public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
    void signUp(UserSignUpRequest signUpRequest);
}
