package com.wilderBackend.security.filter;

import com.wilderBackend.security.jwt.JwtUtils;
import io.jsonwebtoken.*;
import com.wilderBackend.security.userDetails.UserDetailsServiceImpl;
import com.wilderBackend.security.userSession.service.UserSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Service
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private UserSessionService userSessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = jwtUtils.parseJwt(request.getHeader("Authorization"));
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Extract information from the JWT
                Integer userId = jwtUtils.getUserIdFromJwtToken(jwt);
                String sessionId = jwtUtils.getSessionIdFromJwtToken(jwt);

                setSecurityContext(userId, sessionId, username, request);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContext(Integer userId, String sessionId, String username, HttpServletRequest request) {
        // Check if the session is still active in the database
        if (userSessionService.isSessionValid(Long.valueOf(userId), sessionId)) {
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set the authentication for the current request
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("Session for user '{}' and session ID '{}' is invalid.", userId, sessionId);
            throw new ExpiredJwtException(null, null, "Session expired");
        }
    }

}

