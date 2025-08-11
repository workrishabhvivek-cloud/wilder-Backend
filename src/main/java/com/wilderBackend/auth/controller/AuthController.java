package com.wilderBackend.auth.controller;

import com.wilderBackend.auth.request.LoginRequest;
import com.wilderBackend.auth.service.AuthService;
import com.wilderBackend.response.AuthResponse;
import com.wilderBackend.response.Response;
import com.wilderBackend.utils.ExceptionUtils;
import com.wilderBackend.utils.ResponseUtils;
import com.wilderBackend.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody LoginRequest loginRequest, BindingResult bindingResult) {

        // Call the static validateRequest method
        RequestValidator.validateRequest(bindingResult);

        try {
            AuthResponse authResponse = authService.login(loginRequest);
            return ResponseUtils.data(authResponse);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {

        try {
            authService.logout(token);
            return ResponseUtils.data("Logout Successfully.");
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }
}
