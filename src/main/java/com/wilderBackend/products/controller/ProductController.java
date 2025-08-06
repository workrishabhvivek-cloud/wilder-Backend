package com.wilderBackend.products.controller;

import com.wilderBackend.auth.request.LoginRequest;
import com.wilderBackend.response.JwtResponse;
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
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/all")
    public ResponseEntity<Response> getAllProducts() {

        try {
            JwtResponse jwtResponse =  null;
            return ResponseUtils.data(jwtResponse);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }
}
