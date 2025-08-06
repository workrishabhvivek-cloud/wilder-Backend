package com.wilderBackend.sections.repository;

import com.wilderBackend.sections.dto.SectionDTO;
import com.wilderBackend.sections.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("""
        SELECT new com.wilderBackend.sections.dto.SectionDTO(
          s.id,
          s.imageTitle,
          s.imageUrl
        )
        FROM Section s
        WHERE s.imageKey = :imageKey
          AND s.isActive = :isActive
        """)
    List<SectionDTO> findByImageKeyAndIsActive(
            @Param("imageKey") String imageKey,
            @Param("isActive") boolean isActive
    );}
