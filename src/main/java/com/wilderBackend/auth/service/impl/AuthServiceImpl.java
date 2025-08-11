package com.wilderBackend.auth.service.impl;

import com.wilderBackend.auth.request.LoginRequest;
import com.wilderBackend.auth.service.AuthService;
import com.wilderBackend.entity.MasterUser;
import com.wilderBackend.entity.RbacRole;
import com.wilderBackend.exception.ResourceNotFoundException;
import com.wilderBackend.repository.MasterUserRepository;
import com.wilderBackend.repository.RbacRoleRepository;
import com.wilderBackend.response.AuthResponse;
import com.wilderBackend.security.jwt.JwtUtils;
import com.wilderBackend.security.userDetails.UserDetailsImpl;
import com.wilderBackend.security.userSession.entity.UserSession;
import com.wilderBackend.security.userSession.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserSessionService userSessionService;

    private final JwtUtils jwtUtils;

    private final MasterUserRepository masterUserRepository;

    private final RbacRoleRepository rbacRoleRepository;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(), loginRequest.getPassword()));

        // Set authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create user session
        UserSession userSession = userSessionService.createSession(authentication);

        // Generate JWT
        String jwt = jwtUtils.generateJwtToken(authentication, userSession.getSessionId(), userSession.getExpiresAt());

        // Get user details
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Validate if the role is not "Super Admin" then check if the user has the required role or not
        // If the user is "Super Admin", no need to check roles
        if (!roles.contains("Super Admin")){
            MasterUser masterUser = masterUserRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Validate roles
            RbacRole rbacRoles = rbacRoleRepository.findById(masterUser.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            if (!roles.contains(rbacRoles.getName())) {
                throw new BadCredentialsException("Credential does not match");
            }
        }

        // Create AuthResponse with user details and JWT
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(jwt);
        authResponse.setUserName(userDetails.getFirstName() + " " + userDetails.getLastName());
        authResponse.setUserEmail(userDetails.getEmail());
        authResponse.setUserPhoneNumber(userDetails.getPhoneNumber());
        authResponse.setUserRole(userDetails.getRole());
        authResponse.setUserId(userDetails.getId());

        return authResponse;
    }

    @Override
    public void logout(String token) {

        String jwtToken = jwtUtils.parseJwt(token);
        int userId = jwtUtils.getUserIdFromJwtToken(jwtToken);
        String sessionId = jwtUtils.getSessionIdFromJwtToken(jwtToken);

        // Delete the session
        userSessionService.deleteSession((long) userId, sessionId);
    }
}
