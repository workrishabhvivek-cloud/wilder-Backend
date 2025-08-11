package com.wilderBackend.auth.service;


import com.wilderBackend.auth.request.LoginRequest;
import com.wilderBackend.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    void logout(String token);

}
