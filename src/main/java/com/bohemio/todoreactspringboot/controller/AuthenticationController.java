package com.bohemio.todoreactspringboot.controller;

import com.bohemio.todoreactspringboot.dto.JwtResponse;
import com.bohemio.todoreactspringboot.dto.LoginRequest;
import com.bohemio.todoreactspringboot.dto.UserSignUpRequest;
import com.bohemio.todoreactspringboot.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try{
            JwtResponse jwtResponse = authService.login(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (AuthenticationException e){
            return ResponseEntity.status(401).body("Authentication Failed: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserSignUpRequest userSignUpRequest) {
        try{
            authService.signUp(userSignUpRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PostMapping("/authenticate")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.username(),
//                            loginRequest.password()
//                    )
//            );
//
//            String jwt = jwtTokenProvider.generateToken( authentication.getName() );
//            return ResponseEntity.ok( new JwtResponse( jwt ) );
//
//        } catch(AuthenticationException e) {
//            return ResponseEntity.status(401).body("Authentication Failed: " + e.getMessage());
//        }
//
//
//    }

}
