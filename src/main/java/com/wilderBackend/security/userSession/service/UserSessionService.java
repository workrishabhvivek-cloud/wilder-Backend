package com.wilderBackend.security.userSession.service;

import com.wilderBackend.security.userDetails.UserDetailsImpl;
import com.wilderBackend.security.userSession.entity.UserSession;
import com.wilderBackend.security.userSession.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionService {

    @Value("${tms.app.jwtExpirationMs}")
    private Long jwtExpirationMs;

    private final UserSessionRepository userSessionRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int SESSION_ID_LENGTH = 10;

    public UserSession createSession(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

          // delete records from user_sessions by user id
//        userSessionRepository.deleteByUserId(userPrincipal.getId());

        UserSession newUserSession = new UserSession();
        newUserSession.setUserId(userPrincipal.getId());
        newUserSession.setSessionId(generateSessionId());
        newUserSession.setExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs));
        newUserSession.setStatus("Active");
        newUserSession.setCreatedAt(LocalDateTime.now());
        newUserSession.setCreatedBy(userPrincipal.getId());

        return userSessionRepository.save(newUserSession);
    }

    public boolean isSessionValid(Long userId, String sessionId) {
        Optional<UserSession> userSession = userSessionRepository.findValidUserSession(userId, sessionId);
        return userSession.isPresent();
    }

    public static String generateSessionId() {
        Random random = new Random();
        StringBuilder sessionId = new StringBuilder(SESSION_ID_LENGTH);
        for (int i = 0; i < SESSION_ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sessionId.append(CHARACTERS.charAt(index));
        }
        return sessionId.toString();
    }

    @Transactional
    public void deleteSession(Long userId, String sessionId) {
        userSessionRepository.deleteAllByUserIdAndSessionId(userId, sessionId);
    }

}

