package com.wilderBackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userRole;
    private String userPhoneNumber;

}