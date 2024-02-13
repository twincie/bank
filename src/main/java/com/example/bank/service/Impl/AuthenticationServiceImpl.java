package com.example.bank.service.Impl;

import com.example.bank.dto.JwtAuthenticationResponse;
import com.example.bank.dto.RefreshTokenRequest;
import com.example.bank.dto.SignUpRequest;
import com.example.bank.dto.SigninRequest;
import com.example.bank.entity.Role;
import com.example.bank.entity.Users;
import com.example.bank.repository.UsersRepository;
import com.example.bank.service.AuthenticationService;
import com.example.bank.service.JWTService;
import com.example.bank.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;


    public Users signup(SignUpRequest signUpRequest){
        Users users = new Users();

        users.setEmail(signUpRequest.getEmail());
        users.setUsername(signUpRequest.getUsername());
        users.setRole(Role.USER);
        users.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return usersService.create(users);
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest){
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword());
            System.out.println(usernamePasswordAuthenticationToken);
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            var users = usersRepository.findByUsername(signinRequest.getUsername()).orElseThrow(()-> new IllegalArgumentException("Invalid email or password."));
            var jwt = jwtService.generateToken(users);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), users);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshToken);
            return jwtAuthenticationResponse;
        } catch (AuthenticationException e){
            throw new IllegalArgumentException("Authentication failed: "+ e.getMessage());
        }
    }

    public JwtAuthenticationResponse requestToken(RefreshTokenRequest refreshTokenRequest){
        String userName = jwtService.extractUserName(refreshTokenRequest.getToken());
        Users user = usersRepository.findByUsername(userName).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }
}
