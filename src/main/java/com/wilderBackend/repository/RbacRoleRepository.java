package com.wilderBackend.repository;

import com.wilderBackend.entity.RbacRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RbacRoleRepository extends JpaRepository<RbacRole, Long> {

    List<RbacRole> findByName(String name);
}
