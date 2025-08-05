package com.wilderBackend.auth.service.impl;

import com.wilderBackend.auth.request.LoginRequest;
import com.wilderBackend.auth.service.AuthService;
import com.wilderBackend.entity.MasterUser;
import com.wilderBackend.entity.RbacRole;
import com.wilderBackend.exception.ResourceNotFoundException;
import com.wilderBackend.repository.MasterUserRepository;
import com.wilderBackend.repository.RbacRoleRepository;
import com.wilderBackend.response.JwtResponse;
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
    public JwtResponse login(LoginRequest loginRequest) {
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

        if (!roles.contains("Super Admin")){
            MasterUser masterUser = masterUserRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Validate roles
            List<RbacRole> rbacRoles = rbacRoleRepository.findByName(roles.iterator().next());
            if (!roles.contains(rbacRoles.getFirst().getName())) {
                throw new BadCredentialsException("Credential does not match");
            }
        }

        return new JwtResponse(jwt);
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
