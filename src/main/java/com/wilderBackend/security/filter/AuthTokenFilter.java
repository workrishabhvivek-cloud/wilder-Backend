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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

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
//
//        log.info("AuthTokenFilter.doFilterInternal - thread={} uri={} headers={}",
//                Thread.currentThread().getName(), request.getRequestURI(),
//                Collections.list(request.getHeaderNames()).stream()
//                        .collect(Collectors.toMap(h -> h, request::getHeader)));

        try {
            String jwt = jwtUtils.parseJwt(request.getHeader("Authorization"));
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Extract information from the JWT
                Integer userId = jwtUtils.getUserIdFromJwtToken(jwt);
                String sessionId = jwtUtils.getSessionIdFromJwtToken(jwt);

//                log.info("will set security context for username={}, userId={}, sessionId={}", username, userId, sessionId);

                setSecurityContext(userId, sessionId, username, request);
//                log.info("SecurityContext after set: {}", SecurityContextHolder.getContext().getAuthentication());
            }
        }  catch (ExpiredJwtException exception) {
//            log.warn("token expired: {}", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
//            log.error("AuthTokenFilter unexpected error", exception);
            throw exception;
        }

        filterChain.doFilter(request, response);
//        log.info("AuthTokenFilter chain.doFilter returned for uri={}", request.getRequestURI());

    }

    private void setSecurityContext(Integer userId, String sessionId, String username, HttpServletRequest request) {
        // Check if the session is still active in the database
        if (userSessionService.isSessionValid(Long.valueOf(userId), sessionId)) {
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set the authentication for the current request
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // NEW context
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        } else {
            log.warn("Session for user '{}' and session ID '{}' is invalid.", userId, sessionId);
            throw new ExpiredJwtException(null, null, "Your session has expired. Please sign in again to continue.");
        }
    }

}

