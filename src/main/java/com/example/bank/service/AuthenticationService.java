package com.example.bank.service;

import com.example.bank.dto.JwtAuthenticationResponse;
import com.example.bank.dto.RefreshTokenRequest;
import com.example.bank.dto.SignUpRequest;
import com.example.bank.dto.SigninRequest;
import com.example.bank.entity.Users;

public interface AuthenticationService {

    Users signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse requestToken(RefreshTokenRequest refreshTokenRequest);
}
