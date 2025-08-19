package com.wilderBackend.security.userSession.repository;

import com.wilderBackend.security.userSession.entity.UserSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    @Query("SELECT us FROM UserSession us WHERE us.userId = :userId AND us.sessionId = :sessionId AND us.expiresAt > CURRENT_TIMESTAMP")
    Optional<UserSession> findValidUserSession(Long userId, String sessionId);

    void deleteAllByUserIdAndSessionId(Long userId, String sessionId);

    @Query("SELECT u FROM UserSession u ORDER BY u.id ASC")
    List<UserSession> findAllStreamList(Pageable pageable);

}
