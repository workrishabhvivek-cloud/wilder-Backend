package com.wilderBackend.auth.service;


import com.wilderBackend.auth.request.LoginRequest;
import com.wilderBackend.response.JwtResponse;

public interface AuthService {

    JwtResponse login(LoginRequest loginRequest);

    void logout(String token);

}
