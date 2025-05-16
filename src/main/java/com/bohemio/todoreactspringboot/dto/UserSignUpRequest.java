package com.bohemio.todoreactspringboot.dto;

public record UserSignUpRequest(String username, String password, String email) {
}
