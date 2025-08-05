package com.wilderBackend.repository;

import com.wilderBackend.entity.MasterUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MasterUserRepository extends JpaRepository<MasterUser, Long> {

    @Query(value = "SELECT rr.name AS slug " +
            "FROM master_users mu " +
            "LEFT JOIN rbac_roles rr ON mu.role_id = rr.id " +
            "WHERE mu.id = :userId " +
            "LIMIT 1", nativeQuery = true)
    String findRoleSlugByUserId(Long userId);

    Optional<MasterUser> findByEmailAndStatus(String username, String active);
}
