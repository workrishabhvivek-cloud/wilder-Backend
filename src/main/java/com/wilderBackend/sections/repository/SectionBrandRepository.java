package com.wilderBackend.sections.repository;

import com.wilderBackend.sections.dto.BannerDTO;
import com.wilderBackend.sections.entity.SectionBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionBrandRepository extends JpaRepository<SectionBrand, Long> {

    @Query("""
        SELECT new com.wilderBackend.sections.dto.BannerDTO(
          sb.id,
          sb.name,
          sb.imageUrl
        )
        FROM SectionBrand sb
        WHERE sb.status = :status
        """)
    List<BannerDTO> findByStatus(
            @Param("status") String status
    );

}
